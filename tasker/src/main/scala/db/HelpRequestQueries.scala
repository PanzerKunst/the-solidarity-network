package db

import scalikejdbc.ConnectionPool
import anorm._
import models._
import java.util.Date
import models.Country
import scala.Some
import models.HelpRequest
import com.typesafe.scalalogging.slf4j.Logging

object HelpRequestQueries extends Logging {
  def selectIdOfLastProcessedHelpRequest(frequency: String): Long = {
    implicit val c = ConnectionPool.borrow()

    val query = """
      SELECT id_of_last_processed_request
      FROM email_processing_help_request
      WHERE frequency = """" + frequency + """";"""

    logger.info("HelpRequestQueries.selectIdOfLastProcessedHelpRequest():" + query)

    try {
      SQL(query).apply().head[Long]("id_of_last_processed_request")
    }
    finally {
      c.close()
    }
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
            where hr.id > """ + selectIdOfLastProcessedHelpRequest(frequency) + """
            order by hr.id;"""

    logger.info("HelpRequestQueries.selectNonProcessedHelpRequests():" + query)

    try {
      SQL(query)().map(row =>
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
    }
    finally {
      c.close()
    }
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

    logger.info("HelpRequestQueries.selectSubscribers():" + query)

    try {
      SQL(query)().map(row =>
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
    }
    finally {
      c.close()
    }
  }

  def updateLastProcessedRequest(frequency: String, id: Long) {
    implicit val c = ConnectionPool.borrow()

    val query = """
        update email_processing_help_request set
        id_of_last_processed_request = """ + id + """
        where frequency = """" + frequency + """";"""

    logger.info("HelpRequestQueries.updateLastProcessedRequest():" + query)

    try {
      SQL(query).executeUpdate()
    }
    finally {
      c.close()
    }
  }

  def selectSubscribersWillingToBeNotifiedOfNewRequest(request: TaskerHelpRequest): List[TaskerUser] = {
    implicit val c = ConnectionPool.borrow()

    val query = """
      select u.id, u.first_name, u.last_name, u.username, u.email, u.city, u.country_id, u.is_subscribed_to_news, u.subscription_to_new_help_requests,
      c.id, c.name
      from user u
      inner join country c on c.id = u.country_id
      where u.id != """ + request.requester.id + """
      and subscription_to_new_help_requests = """" + User.NEW_HR_SUBSCRIPTION_FREQUENCY_EACH_NEW_REQUEST + """"
      and u.city = """" + request.requester.city + """"
      and u.country_id = """ + request.requester.country.id + """;"""

    logger.info("HelpRequestQueries.selectSubscribersWillingToBeNotifiedOfNewRequest():" + query)

    try {
      SQL(query)().map(row =>
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
    }
    finally {
      c.close()
    }
  }
}
