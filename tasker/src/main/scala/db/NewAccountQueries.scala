package db

import scalikejdbc.ConnectionPool
import anorm._
import models._
import java.util.Date
import scala.Some
import models.HelpRequest
import com.typesafe.scalalogging.slf4j.Logging

object NewAccountQueries extends Logging {
  def selectIdOfLastProcessedNewAccount(): Long = {
    implicit val c = ConnectionPool.borrow()

    val query = """
      SELECT id_of_last_processed_new_account
      FROM email_processing_new_account;"""

    logger.info("NewAccountQueries.selectIdOfLastProcessedNewAccount():" + query)

    try {
      SQL(query).apply().head[Long]("id_of_last_processed_new_account")
    }
    finally {
      c.close()
    }
  }

  def selectNonProcessedNewAccounts(): List[User] = {
    implicit val c = ConnectionPool.borrow()

    val query = """
            select id, first_name, last_name, username, email, city, country_id, is_subscribed_to_news, subscription_to_new_help_requests
            from user
            where id > """ + selectIdOfLastProcessedNewAccount + """
            order by id;"""

    logger.info("NewAccountQueries.selectNonProcessedNewAccounts():" + query)

    try {
      SQL(query)().map(row =>
        (
            User(
              id = Some(row[Long]("id")),
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
      ).toList
    }
    finally {
      c.close()
    }
  }

  def updateLastProcessedNewAccount(id: Long) {
    implicit val c = ConnectionPool.borrow()

    val query = """
        update email_processing_new_account set
        id_of_last_processed_new_account = """ + id + """;"""

    logger.info("NewAccountQueries.updateLastProcessedNewAccount():" + query)

    try {
      SQL(query).executeUpdate()
    }
    finally {
      c.close()
    }
  }
}
