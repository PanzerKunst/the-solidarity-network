package database

import anorm._
import play.api.db.DB
import models.HelpRequest
import play.api.Play.current
import play.api.Logger

object HelpRequestDto {
  def create(helpRequest: HelpRequest) {
    DB.withConnection {
      implicit c =>

        val query = """
                       insert into help_request(description, requester_id, expiry_date)
      values("""" + DbUtil.backslashQuotes(helpRequest.description) + """",""" +
          helpRequest.requesterId + ""","""" +
          DbUtil.dateToString(helpRequest.expiryDate) + """");"""

        Logger.info("HelpRequestDto.create(): " + query)

        SQL(query).execute()
    }
  }
}
