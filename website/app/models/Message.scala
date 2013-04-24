package models

import java.util.Date

case class Message(id: Option[Long] = None,
                   fromUserId: Option[Long] = None,
                   toUserId: Long,
                   title: Option[String],
                   text: String,
                   creationDatetime: Option[Date],
                   replyToMessageId: Option[Long])