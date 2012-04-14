package database

import anorm._
import play.api.db.DB
import models.HelpRequest
import play.api.Play.current

object HelpRequestDto {
  def create(helpRequest: HelpRequest) {
    DB.withConnection {
      implicit c =>

        SQL("""insert into help_request(description, requester_id, expiry_date)
      values("""" + DbUtil.backslashQuotes(helpRequest.description) + """",""" +
          helpRequest.requesterId + ""","""" +
          DbUtil.dateToString(helpRequest.expiryDate) + """");"""
        )
          .execute()
    }
  }
}
