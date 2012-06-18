package database

import anorm._
import play.api.db.DB
import models.HelpRequest
import play.api.Play.current
import play.api.Logger
import java.util

object HelpRequestDto {
  def get(filters: Option[Map[String, String]]): List[HelpRequest] = {
    DB.withConnection {
      implicit c =>

        val query = """
          select id, title, description, requester_id, creation_date, expiry_date
          from help_request """ + DbUtil.generateWhereClause(filters) + ";"

        Logger.info("HelpRequestDto.getHelpRequets():" + query)

        SQL(query)().map(row =>
          new HelpRequest(
            row[Long]("id"),
            row[Long]("requester_id"),
            row[String]("title"),
            row[String]("description"),
            row[util.Date]("creation_date"),
            row[util.Date]("expiry_date")
          )
        ).toList
    }
  }

  def create(helpRequest: HelpRequest) {
    DB.withConnection {
      implicit c =>

        val query = """
                       insert into help_request(requester_id, title, description, creation_date, expiry_date)
      values(""" + helpRequest.requesterId + ""","""" +
          DbUtil.backslashQuotes(helpRequest.title) + """","""" +
          DbUtil.backslashQuotes(helpRequest.description) + """","""" +
          DbUtil.dateToString(new util.Date()) + """","""" +
          DbUtil.dateToString(helpRequest.expiryDate) + """");"""

        Logger.info("HelpRequestDto.create(): " + query)

        SQL(query).execute()
    }
  }
}
