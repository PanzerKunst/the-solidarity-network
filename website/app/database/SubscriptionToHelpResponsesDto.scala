package database

import anorm._
import play.api.db.DB
import play.api.Play.current
import models.{SubscriptionToHelpResponses, ReferenceRating}
import play.api.Logger
import controllers.InconsistentDataException


object SubscriptionToHelpResponsesDto {
  def get(filters: Option[Map[String, String]]): List[SubscriptionToHelpResponses] = {
    DB.withConnection {
      implicit c =>

        val query = """
          select id, request_id, subscriber_id
          from subscription_to_help_responses """ + DbUtil.generateWhereClause(filters) + ";"

        Logger.info("SubscriptionToHelpResponsesDto.get():" + query)

        SQL(query)().map(row =>
          SubscriptionToHelpResponses(
            id = Some(row[Long]("id")),
            requestId = row[Long]("request_id"),
            subscriberId = Some(row[Long]("subscriber_id"))
          )
        ).toList
    }
  }

  def create(subscriptionToHelpResponses: SubscriptionToHelpResponses): Option[Long] = {
    DB.withConnection {
      implicit c =>

        val query = """
                       insert into subscription_to_help_responses(request_id, subscriber_id)
      values(""" + subscriptionToHelpResponses.requestId + """, """ +
          subscriptionToHelpResponses.subscriberId.get + """);"""

        Logger.info("SubscriptionToHelpResponsesDto.create(): " + query)

        SQL(query).executeInsert()
    }
  }

  def delete(subscriptionToHelpResponses: SubscriptionToHelpResponses) {
    DB.withConnection {
      implicit c =>

        val query = """
                       delete subscription_to_help_responses
      where request_id = """ + subscriptionToHelpResponses.requestId + """,
          and subscriber_id = """ + subscriptionToHelpResponses.subscriberId.get + """;"""

        Logger.info("SubscriptionToHelpResponsesDto.delete(): " + query)

        val rowsUpdatedCount = SQL(query).executeUpdate()

        if (rowsUpdatedCount != 1)
          throw new InconsistentDataException("Trying to delete a subscription but executeUpdate() updated " + rowsUpdatedCount + " rows instead of 1!")
    }
  }
}
