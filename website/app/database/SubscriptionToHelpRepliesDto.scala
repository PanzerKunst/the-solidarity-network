package database

import anorm._
import play.api.db.DB
import play.api.Play.current
import models.SubscriptionToHelpReplies
import play.api.Logger
import controllers.InconsistentDataException


object SubscriptionToHelpRepliesDto {
  def get(filters: Option[Map[String, String]]): List[SubscriptionToHelpReplies] = {
    DB.withConnection {
      implicit c =>

        val query = """
          select id, request_id, subscriber_id
          from subscription_to_help_replies """ + DbUtil.generateWhereClause(filters) + ";"

        Logger.info("SubscriptionToHelpRepliesDto.get():" + query)

        SQL(query)().map(row =>
          SubscriptionToHelpReplies(
            id = Some(row[Long]("id")),
            requestId = row[Long]("request_id"),
            subscriberId = Some(row[Long]("subscriber_id"))
          )
        ).toList
    }
  }

  def create(subscriptionToHelpReplies: SubscriptionToHelpReplies): Option[Long] = {
    DB.withConnection {
      implicit c =>

        val query = """
                       insert into subscription_to_help_replies(request_id, subscriber_id)
      values(""" + subscriptionToHelpReplies.requestId + """, """ +
          subscriptionToHelpReplies.subscriberId.get + """);"""

        Logger.info("SubscriptionToHelpRepliesDto.create(): " + query)

        SQL(query).executeInsert()
    }
  }

  def delete(subscriptionToHelpReplies: SubscriptionToHelpReplies) {
    DB.withConnection {
      implicit c =>

        val query = """
                       delete from subscription_to_help_replies
          where request_id = """ + subscriptionToHelpReplies.requestId + """
          and subscriber_id = """ + subscriptionToHelpReplies.subscriberId.get + """;"""

        Logger.info("SubscriptionToHelpRepliesDto.delete(): " + query)

        val rowsUpdatedCount = SQL(query).executeUpdate()

        if (rowsUpdatedCount != 1)
          throw new InconsistentDataException("Trying to delete a subscription but executeUpdate() updated " + rowsUpdatedCount + " rows instead of 1!")
    }
  }
}
