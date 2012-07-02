package database

import anorm._
import play.api.db.DB
import models.HelpResponse
import play.api.Play.current
import play.api.Logger
import java.util

object HelpResponseDto {
  def get(filters: Option[Map[String, String]]): List[HelpResponse] = {
    DB.withConnection {
      implicit c =>

        val query = """
          select id, request_id, responder_id, text, creation_date
          from help_response """ + DbUtil.generateWhereClause(filters) + """
          order by creation_date desc;"""

        Logger.info("HelpResponseDto.get():" + query)

        SQL(query)().map(row =>
          new HelpResponse(
            row[Long]("id"),
            row[Long]("request_id"),
            row[Long]("responder_id"),
            row[String]("text"),
            row[util.Date]("creation_date")
          )
        ).toList
    }
  }

  def create(helpResponse: HelpResponse) {
    DB.withConnection {
      implicit c =>

        val query = """
                       insert into help_response(request_id, responder_id, text, creation_date)
      values(""" + helpResponse.requestId + """,""" +
          helpResponse.responderId + ""","""" +
          DbUtil.backslashQuotes(helpResponse.text) + """","""" +
          DbUtil.dateToString(new util.Date()) + """");"""

        Logger.info("HelpResponseDto.create(): " + query)

        SQL(query).execute()
    }
  }
}