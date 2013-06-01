package db

import anorm._
import play.api.db.DB
import play.api.Play.current
import models.Message
import play.api.Logger
import java.util.Date


object MessageDto {
  def get(filters: Map[String, String], orderIsDesc: Boolean = true): List[Message] = {
    DB.withConnection {
      implicit c =>

        val orderForQuery = if (orderIsDesc) "desc" else ""

        val query = """
          select id, from_user_id, to_user_id, title, text, creation_date, reply_to_message_id, is_read
          from message """ + DbUtil.generateWhereClause(Some(filters)) + """
          order by creation_date """ + orderForQuery + """;"""

        Logger.info("MessageDto.get():" + query)

        SQL(query)().map(row =>
          Message(
            id = Some(row[Long]("id")),
            fromUserId = Some(row[Long]("from_user_id")),
            toUserId = row[Long]("to_user_id"),
            title = row[Option[String]]("title"),
            text = row[String]("text"),
            creationDatetime = Some(row[Date]("creation_date")),
            replyToMessageId = row[Option[Long]]("reply_to_message_id"),
            isRead = row[Boolean]("is_read")
          )
        ).toList
    }
  }

  def create(message: Message): Option[Long] = {
    DB.withConnection {
      implicit c =>

        var messageTitleForQuery = "NULL"
        if (message.title.isDefined && message.title.get != "")
          messageTitleForQuery = "\"" + DbUtil.backslashQuotes(message.title.get) + "\""

        val messageTextForQuery = message.text.replaceAll("\n", "\\\\n")

        val query = """
                       insert into message(from_user_id, to_user_id, title, text, creation_date, reply_to_message_id)
        values(""" + message.fromUserId.get + """, """ +
          message.toUserId + """, """ +
          messageTitleForQuery + """, """" +
          DbUtil.backslashQuotes(messageTextForQuery) + """", """" +
          DbUtil.datetimeToString(new Date()) + """",""" +
          message.replyToMessageId.getOrElse("NULL") + """);"""

        Logger.info("MessageDto.create(): " + query)

        SQL(query).executeInsert()
    }
  }

  def markAsRead(message: Message) {
    DB.withConnection {
      implicit c =>

        val query = """
            update message
            set is_read = true
            where id = """ + message.id.get + """;"""

        SQL(query).executeUpdate()
    }
  }
}
