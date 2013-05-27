import akka.actor.{Props, Actor}
import anorm._
import com.typesafe.scalalogging.slf4j.Logging
import java.util.Date
import models.frontend.{FrontendHelpRequest, FrontendUser}
import models.{Country, HelpRequest, User}
import scalikejdbc.ConnectionPool

object HelpRequestSubscriptionTasker extends Logging {
  val weeklyTick = 0
  val dailyTick = 1
  val eachRequestTick = 2

  val tickActor = Tasker.system.actorOf(Props(new Actor {
    def receive = {
      case weeklyTick ⇒
        val nonProcessedHelpRequests: List[FrontendHelpRequest] = selectNonProcessedHelpRequests(User.NEW_HR_SUBSCRIPTION_FREQUENCY_WEEKLY)

        for (subscriber <- selectSubscribers(User.NEW_HR_SUBSCRIPTION_FREQUENCY_WEEKLY)) {
          val helpRequestsInSameCity = nonProcessedHelpRequests.filter(hr => hr.requester.city == subscriber.city && hr.requester.country.id == subscriber.country.id)
          EmailService.sendWeeklyHelpRequestSubscriptionEmail(subscriber, helpRequestsInSameCity)
        }
    }
  }))

  private def idOfLastProcessedHelpRequest(frequency: String): Long = {
    implicit val c = ConnectionPool.borrow()

    val query = """
      SELECT id_of_last_processed_request
      FROM email_processing_help_request
      WHERE frequency = """" + frequency + """"
                                           """

    logger.info("HelpRequestSubscriptionTasker.idOfLastProcessedHelpRequest():" + query)

    val rows = SQL(query).apply()

    if (rows.length > 0)
      rows.head[Long]("id_of_last_processed_request")
    else
      0
  }

  /**
   * We return FrontendHelpRequest instead of HelpRequest for performance reasons: it reduces number of SQL requests
   * @return the list
   */
  private def selectNonProcessedHelpRequests(frequency: String): List[FrontendHelpRequest] = {
    implicit val c = ConnectionPool.borrow()

    val query = """
            select distinct hr.id, hr.title, hr.description, hr.creation_date, hr.expiry_date,
              u.id, u.first_name, u.last_name, u.username, u.email, u.city, u.country_id, u.is_subscribed_to_news, u.subscription_to_new_help_requests,
              c.id, c.name
            from help_request hr
            inner join user u on u.id = hr.requester_id
            inner join country c on c.id = u.country_id
            where hr.id > """ + idOfLastProcessedHelpRequest(frequency) + """
            order by expiry_date, creation_date;"""

    logger.info("HelpRequestSubscriptionTasker.selectNonProcessedWeeklyHelpRequests():" + query)

    SQL(query)().map(row =>
      (
        new FrontendHelpRequest(
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
          )
        )
        )
    ).toList
  }

  /**
   * We return FrontendUser instead of User for performance reasons: it reduces number of SQL requests
   * @return
   */
  def selectSubscribers(frequency: String): List[FrontendUser] = {
    implicit val c = ConnectionPool.borrow()

    val query = """
            select u.id, u.first_name, u.last_name, u.username, u.email, u.city, u.country_id, u.is_subscribed_to_news, u.subscription_to_new_help_requests,
            c.id, c.name
            from user u
            inner join country c on c.id = u.country_id
            where subscription_to_new_help_requests = """" + frequency + """";"""

    logger.info("HelpRequestSubscriptionTasker.selectWeeklySubscribers():" + query)

    SQL(query)().map(row =>
      (
        new FrontendUser(row[Long]("user.id"),
          row[String]("first_name"),
          row[String]("last_name"),
          row[String]("username"),
          row[String]("email"),
          row[String]("city"),
          Country(
            row[Long]("country.id"),
            row[String]("name")
          ),
          row[Boolean]("is_subscribed_to_news"),
          row[String]("subscription_to_new_help_requests")
        )
        )
    ).toList
  }
}
