package db

import scalikejdbc.ConnectionPool
import anorm._
import models._
import scala.Some
import com.typesafe.scalalogging.slf4j.Logging
import java.util.Date

object MessageQueries extends Logging {
  def selectIdOfLastProcessedMessage(): Long = {
    implicit val c = ConnectionPool.borrow()

    val query = """
      SELECT id_of_last_processed_message
      FROM email_processing_message;"""

    logger.info("MessageQueries.selectIdOfLastProcessedMessage():" + query)

    try {
      SQL(query).apply().head[Long]("id_of_last_processed_message")
    }
    finally {
      c.close()
    }
  }

  /**
   * I can't join twice on table user in the same query because of a JDBC bug with MySQL
   * @see https://groups.google.com/forum/?fromgroups=#!topic/play-framework/DXbOAYz-cM0
   * @return
   */
  def selectNonProcessedMessages(): IndexedSeq[TaskerMessage] = {
    implicit val c = ConnectionPool.borrow()

    val query = """
            select distinct m.id, m.from_user_id, m.to_user_id, m.title, m.text, m.creation_date, m.reply_to_message_id, m.is_read,
              u.id, u.first_name, u.last_name, u.username, u.email, u.is_subscribed_to_news, u.subscription_to_new_help_requests
            from message m
            inner join user u on u.id = from_user_id
            where m.id > """ + selectIdOfLastProcessedMessage + """
            order by m.id;"""

    logger.info("MessageQueries.selectNonProcessedMessages() - 1/2:" + query)

    try {
      val listOfMessageAndFromUser: List[(Message, User)] = SQL(query)().map(row =>
        (
          Message(
            id = Some(row[Long]("message.id")),
            fromUserId = Some(row[Long]("from_user_id")),
            toUserId = row[Long]("to_user_id"),
            title = row[Option[String]]("title"),
            text = row[String]("text"),
            creationDatetime = Some(row[Date]("creation_date")),
            replyToMessageId = row[Option[Long]]("reply_to_message_id"),
            isRead = row[Boolean]("is_read")
          ),
          User(
            id = Some(row[Long]("user.id")),
            firstName = Some(row[String]("first_name")),
            lastName = Some(row[String]("last_name")),
            username = Some(row[String]("username")),
            email = Some(row[String]("email")),
            isSubscribedToNews = row[Boolean]("is_subscribed_to_news"),
            subscriptionToNewHelpRequests = row[String]("subscription_to_new_help_requests")
          )
          )
      ).toList

      if (listOfMessageAndFromUser.isEmpty)
        IndexedSeq()
      else {
        var inClause = ""
        var i = 0

        for ((msg, fromUser) <- listOfMessageAndFromUser) {
          inClause = inClause + msg.id.get

          if (i < listOfMessageAndFromUser.length - 1)
            inClause += ","

          i = i + 1
        }

        val query2 = """
        select distinct u.id, u.first_name, u.last_name, u.username, u.email, u.is_subscribed_to_news, u.subscription_to_new_help_requests,
          m.id
        from user u
        inner join message m on m.to_user_id = u.id
        where m.id in (""" + inClause + """)
        order by m.id;"""

        logger.info("MessageQueries.selectNonProcessedMessages() - 2/2:" + query2)

        val toUsers = SQL(query2)().map(row =>
          User(
            id = Some(row[Long]("user.id")),
            firstName = Some(row[String]("first_name")),
            lastName = Some(row[String]("last_name")),
            username = Some(row[String]("username")),
            email = Some(row[String]("email")),
            isSubscribedToNews = row[Boolean]("is_subscribed_to_news"),
            subscriptionToNewHelpRequests = row[String]("subscription_to_new_help_requests")
          )
        ).toList

        for (i <- 0 to listOfMessageAndFromUser.length - 1)
        yield new TaskerMessage(listOfMessageAndFromUser(i)._1, listOfMessageAndFromUser(i)._2, toUsers(i))
      }
    }
    finally {
      c.close()
    }
  }

  def updateLastProcessedMessage(id: Long) {
    implicit val c = ConnectionPool.borrow()

    val query = """
        update email_processing_message set
        id_of_last_processed_message = """ + id + """;"""

    logger.info("MessageQueries.updateLastProcessedMessage():" + query)

    try {
      SQL(query).executeUpdate()
    }
    finally {
      c.close()
    }
  }
}
