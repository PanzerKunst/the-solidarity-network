package database

import anorm._
import play.api.db.DB
import play.api.Play.current
import models.{ReferenceRating, Country}
import play.api.Logger


object ReferenceRatingDto {
  def get(filters: Option[Map[String, String]]): List[ReferenceRating] = {
    DB.withConnection {
      implicit c =>

        val query = """
          select id, label
          from reference_rating """ + DbUtil.generateWhereClause(filters) + ";"

        Logger.info("ReferenceRatingDto.get():" + query)

        SQL(query)().map(row =>
          ReferenceRating(
            id = row[Long]("id"),
            label = row[String]("label")
          )
        ).toList
    }
  }
}
