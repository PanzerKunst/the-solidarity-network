package database

import anorm._
import play.api.db.DB
import play.api.Play.current
import models.Reference
import play.api.Logger
import java.util.Date


object ReferenceDto {
  def get(filters: Option[Map[String, String]]): List[Reference] = {
    DB.withConnection {
      implicit c =>

        val query = """
          select id, from_user_id, to_user_id, was_helped, rating_id, text, creation_date
          from reference """ + DbUtil.generateWhereClause(filters) + ";"

        Logger.info("ReferenceDto.get():" + query)

        SQL(query)().map(row =>
          Reference(
            id = Some(row[Long]("id")),
            fromUserId = Some(row[Long]("from_user_id")),
            toUserId = row[Long]("to_user_id"),
            wasHelped = row[Boolean]("was_helped"),
            ratingId = row[Long]("rating_id"),
            text = row[String]("text"),
            creationDatetime = Some(row[Date]("creation_date"))
          )
        ).toList
    }
  }

  def create(reference: Reference): Option[Long] = {
    DB.withConnection {
      implicit c =>

        val query = """
                       insert into reference(from_user_id, to_user_id, was_helped, rating_id, text, creation_date)
      values(""" + reference.fromUserId.get + """, """ +
          reference.toUserId + """, """ +
          reference.wasHelped.toString + """, """ +
          reference.ratingId + """, """" +
          DbUtil.backslashQuotes(reference.text) + """", """" +
          DbUtil.datetimeToString(new Date()) + """");"""

        Logger.info("ReferenceDto.create(): " + query)

        SQL(query).executeInsert()
    }
  }
}
