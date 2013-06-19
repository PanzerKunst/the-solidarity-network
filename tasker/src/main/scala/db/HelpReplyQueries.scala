package db

import scalikejdbc.ConnectionPool
import anorm._
import models._
import java.util.Date
import scala.Some
import models.HelpRequest
import com.typesafe.scalalogging.slf4j.Logging

object HelpReplyQueries extends Logging {
  def selectIdOfLastProcessedHelpReply(): Long = {
    implicit val c = ConnectionPool.borrow()

    val query = """
      SELECT id_of_last_processed_reply
      FROM email_processing_help_reply;"""

    logger.info("HelpReplyQueries.selectIdOfLastProcessedHelpReply():" + query)

    try {
      SQL(query).apply().head[Long]("id_of_last_processed_reply")
    }
    finally {
      c.close()
    }
  }

  /**
   * We return TaskerHelpReply instead of HelpReply for performance reasons: it reduces number of SQL requests
   * @return the list
   */
  def selectNonProcessedHelpReplies(): List[TaskerHelpReply] = {
    implicit val c = ConnectionPool.borrow()

    val query = """
            select distinct reply.id, reply.request_id, reply.replier_id, reply.text, reply.creation_date,
              hr.id, hr.requester_id, hr.title, hr.description, hr.creation_date, hr.expiry_date,
              u.id, u.first_name, u.last_name, u.username, u.email, u.city, u.country_id, u.is_subscribed_to_news, u.subscription_to_new_help_requests
            from help_reply reply
            inner join help_request hr on hr.id = reply.request_id
            inner join user u on u.id = reply.replier_id
            where reply.id > """ + selectIdOfLastProcessedHelpReply + """
            order by reply.id;"""

    logger.info("HelpReplyQueries.selectNonProcessedHelpReplies():" + query)

    try {
      SQL(query)().map(row =>
        (
          new TaskerHelpReply(
            HelpReply(
              id = Some(row[Long]("help_reply.id")),
              requestId = row[Long]("help_request.id"),
              replierId = Some(row[Long]("replier_id")),
              text = row[String]("text"),
              creationDatetime = Some(row[Date]("help_reply.creation_date"))
            ),
            HelpRequest(
              id = Some(row[Long]("help_request.id")),
              requesterId = Some(row[Long]("requester_id")),
              title = row[String]("title"),
              description = row[String]("description"),
              creationDatetime = Some(row[Date]("help_request.creation_date")),
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
    finally {
      c.close()
    }
  }

  def updateLastProcessedReply(id: Long) {
    implicit val c = ConnectionPool.borrow()

    val query = """
        update email_processing_help_reply set
        id_of_last_processed_reply = """ + id + """;"""

    logger.info("HelpReplyQueries.updateLastProcessedReply():" + query)

    try {
      SQL(query).executeUpdate()
    }
    finally {
      c.close()
    }
  }

  def selectSubscribersWillingToBeNotifiedOfReply(reply: TaskerHelpReply): List[TaskerUser] = {
    implicit val c = ConnectionPool.borrow()

    val query = """
      select u.id, u.first_name, u.last_name, u.username, u.email, u.city, u.country_id, u.is_subscribed_to_news, u.subscription_to_new_help_requests,
      s.id, s.request_id, s.subscriber_id,
      c.id, c.name
      from user u
      inner join subscription_to_help_replies s on s.subscriber_id = u.id
      inner join country c on c.id = u.country_id
      where s.request_id = """ + reply.request.id.get + """
      and u.id != """ + reply.replier.id.get + """;"""

    logger.info("HelpReplyQueries.selectSubscribersWillingToBeNotifiedOfReply():" + query)

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
  }}
