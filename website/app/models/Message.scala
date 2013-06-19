package models

import java.util.Date
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

case class Message(
                    @JsonDeserialize(contentAs = classOf[java.lang.Long])
                    id: Option[Long] = None,

                    @JsonDeserialize(contentAs = classOf[java.lang.Long])
                    fromUserId: Option[Long] = None,

                    toUserId: Long,
                    title: Option[String],
                    text: String,
                    creationDatetime: Option[Date],

                    @JsonDeserialize(contentAs = classOf[java.lang.Long])
                    replyToMessageId: Option[Long],

                    isRead: Boolean)