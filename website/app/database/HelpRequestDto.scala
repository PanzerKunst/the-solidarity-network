package database

import anorm._
import play.api.db.DB
import models.HelpRequest
import play.api.Play.current
import play.api.Logger
import java.util

object HelpRequestDto {
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
