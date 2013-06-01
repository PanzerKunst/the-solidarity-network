package db

import scalikejdbc.ConnectionPool
import anorm._
import models._
import java.util.Date
import models.Country
import scala.Some
import models.HelpRequest
import com.typesafe.scalalogging.slf4j.Logging

object TaskerQueries extends Logging {
  def idOfLastProcessedHelpRequest(frequency: String): Option[Long] = {
    implicit val c = ConnectionPool.borrow()

    val query = """
      SELECT id_of_last_processed_request
      FROM email_processing_help_request
      WHERE frequency = """" + frequency + """";"""

    logger.info("HelpRequestSubscriptionTasker.idOfLastProcessedHelpRequest():" + query)

    val rows = SQL(query).apply()

    val result = if (rows.length > 0)
      Some(rows.head[Long]("id_of_last_processed_request"))
    else
      None

    c.close()

    result
  }

  /**
   * We return TaskerHelpRequest instead of HelpRequest for performance reasons: it reduces number of SQL requests
   * @return the list
   */
  def selectNonProcessedHelpRequests(frequency: String): List[TaskerHelpRequest] = {
    implicit val c = ConnectionPool.borrow()

    val query = """
            select distinct hr.id, hr.title, hr.description, hr.creation_date, hr.expiry_date,
              u.id, u.first_name, u.last_name, u.username, u.email, u.city, u.country_id, u.is_subscribed_to_news, u.subscription_to_new_help_requests,
              c.id, c.name
            from help_request hr
            inner join user u on u.id = hr.requester_id
            inner join country c on c.id = u.country_id
            where hr.id > """ + idOfLastProcessedHelpRequest(frequency).getOrElse(0) + """
            order by hr.id;"""

    logger.info("HelpRequestSubscriptionTasker.selectNonProcessedWeeklyHelpRequests():" + query)

    val result = SQL(query)().map(row =>
      (
        new TaskerHelpRequest(
          HelpRequest(
            id = Some(row[Long]("help_request.id")),
            requesterId = Some(row[Long]("user.id")),
            title = row[String]("title"),
            description = row[String]("description"),
            creationDatetime = Some(row[Date]("creation_date")),
            expiryDate = row[Date]("expiry_date")
          ),
          User(
            id = Some(row[Long]("user.id")),
            firstName = Some(row[String]("first_name")),
            lastName = Some(row[String]("last_name")),
            username = Some(row[String]("username")),
            email = Some(row[String]("email")),
            city = Some(row[String]("city")),
            countryId = Some(row[Long]("country_id")),
            isSubscribedToNews = row[Boolean]("is_subscribed_to_news"),
            subscriptionToNewHelpRequests = row[String]("subscription_to_new_help_requests")
          ),
          Country(
            row[Long]("country.id"),
            row[String]("name")
          )
        )
        )
    ).toList

    c.close()

    result
  }

  /**
   * We return TaskerUser instead of User for performance reasons: it reduces number of SQL requests
   * @return
   */
  def selectSubscribers(frequency: String): List[TaskerUser] = {
    implicit val c = ConnectionPool.borrow()

    val query = """
            select u.id, u.first_name, u.last_name, u.username, u.email, u.city, u.country_id, u.is_subscribed_to_news, u.subscription_to_new_help_requests,
            c.id, c.name
            from user u
            inner join country c on c.id = u.country_id
            where subscription_to_new_help_requests = """" + frequency + """";"""

    logger.info("HelpRequestSubscriptionTasker.selectWeeklySubscribers():" + query)

    val result = SQL(query)().map(row =>
      (
        new TaskerUser(row[Long]("user.id"),
          row[String]("first_name"),
          row[String]("last_name"),
          row[String]("username"),
          row[String]("email"),
          None,
          None,
          row[String]("city"),
          Country(
            row[Long]("country.id"),
            row[String]("name")
          ),
          None,
          row[Boolean]("is_subscribed_to_news"),
          row[String]("subscription_to_new_help_requests")
        )
        )
    ).toList

    c.close()

    result
  }

  def updateLastProcessedRequest(frequency: String, id: Long) {
    implicit val c = ConnectionPool.borrow()

    idOfLastProcessedHelpRequest(frequency) match {
      case Some(idOfLastProcessedHelpRequest) =>
        val query = """
        update email_processing_help_request set
        id_of_last_processed_request = """ + id + """
        where frequency = """" + frequency + """";"""

        logger.info("HelpRequestSubscriptionTasker.updateLastProcessedRequest():" + query)

        SQL(query).executeUpdate()

      case None =>
        val query = """
        insert into email_processing_help_request(frequency, id_of_last_processed_request)
        values ("""" + frequency + """", """ + id + """);"""

        logger.info("HelpRequestSubscriptionTasker.updateLastProcessedRequest():" + query)

        SQL(query).executeInsert()
    }

    c.close()
  }
}
