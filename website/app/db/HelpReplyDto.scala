package db

import anorm._
import play.api.db.DB
import models.HelpReply
import play.api.Play.current
import play.api.Logger
import java.util

object HelpReplyDto {
  def get(filters: Option[Map[String, String]]): List[HelpReply] = {
    DB.withConnection {
      implicit c =>

        val query = """
          select id, request_id, replier_id, text, creation_date
          from help_reply """ + DbUtil.generateWhereClause(filters) + """
          order by creation_date;"""

        Logger.info("HelpReplyDto.get():" + query)

        SQL(query)().map(row =>
          HelpReply(
            id = Some(row[Long]("id")),
            requestId = row[Long]("request_id"),
            replierId = Some(row[Long]("replier_id")),
            text = row[String]("text"),
            creationDatetime = Some(row[util.Date]("creation_date"))
          )
        ).toList
    }
  }

  def create(helpReply: HelpReply): Option[Long] = {
    DB.withConnection {
      implicit c =>

        val textForQuery = helpReply.text.replaceAll("\n", "\\\\n")

        val query = """
                       insert into help_reply(request_id, replier_id, text, creation_date)
      values(""" + helpReply.requestId + """, """ +
          helpReply.replierId.get + """, """" +
          DbUtil.backslashQuotes(textForQuery) + """", """" +
          DbUtil.datetimeToString(new util.Date()) + """");"""

        Logger.info("HelpReplyDto.create(): " + query)

        SQL(query).executeInsert()
    }
  }
}
