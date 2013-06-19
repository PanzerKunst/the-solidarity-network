package models

import org.codehaus.jackson.annotate.JsonProperty
import java.util.Date

class TaskerMessage {
  def this(message: Message, fromUser: User, toUser: User) = {

    this()

    this.id = message.id.get
    this.title = message.title
    this.text = message.text
    this.creationDatetime = message.creationDatetime.get
    this.replyToMessageId = message.replyToMessageId

    this.fromUser = fromUser
    this.toUser = toUser
  }

  @JsonProperty
  var id: Long = _

  @JsonProperty
  var fromUser: User = _

  @JsonProperty
  var toUser: User = _

  @JsonProperty
  var title: Option[String] = _

  @JsonProperty
  var text: String = _

  @JsonProperty
  var creationDatetime: Date = _

  @JsonProperty
  var replyToMessageId: Option[Long] = _

  @JsonProperty
  var isRead: Boolean = _
}
