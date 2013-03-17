package database

import anorm._
import play.api.db.DB
import play.api.Play.current
import models.{Message, Reference}
import play.api.Logger
import java.util.Date


object MessageDto {
  def get(filters: Option[Map[String, String]]): List[Message] = {
    DB.withConnection {
      implicit c =>

        val query = """
          select id, from_user_id, to_user_id, title, text, creation_date
          from message """ + DbUtil.generateWhereClause(filters) + """
          order by creation_date desc;"""

        Logger.info("MessageDto.get():" + query)

        SQL(query)().map(row =>
          Message(
            id = Some(row[Long]("id")),
            fromUserId = Some(row[Long]("from_user_id")),
            toUserId = row[Long]("to_user_id"),
            title = row[String]("title"),
            text = row[String]("text"),
            creationDatetime = Some(row[Date]("creation_date"))
          )
        ).toList
    }
  }

  def create(message: Message): Option[Long] = {
    DB.withConnection {
      implicit c =>

        val query = """
                       insert into message(from_user_id, to_user_id, title, text, creation_date)
      values(""" + message.fromUserId.get + """, """ +
          message.toUserId + """, """" +
          DbUtil.backslashQuotes(message.title) + """", """" +
          DbUtil.backslashQuotes(message.text) + """", """" +
          DbUtil.datetimeToString(new Date()) + """");"""

        Logger.info("MessageDto.create(): " + query)

        SQL(query).executeInsert()
    }
  }
}
