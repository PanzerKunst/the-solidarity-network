package models.frontend

import models.{Message, ReferenceRating, Reference, User}
import database.{DbUtil, ReferenceRatingDto, UserDto}
import org.codehaus.jackson.annotate.JsonProperty

class FrontendMessage {
  def this(message: Message) = {
    this()

    this.id = message.id.get
    this.fromUser = new FrontendUser(UserDto.get(Some(Map("id" -> message.fromUserId.get.toString))).head)
    this.toUser = new FrontendUser(UserDto.get(Some(Map("id" -> message.toUserId.toString))).head)
    this.title = message.title
    this.text = message.text
    this.creationDatetime = DbUtil.datetimeToString(message.creationDatetime.get)
  }

  @JsonProperty
  var id: Long = _

  @JsonProperty
  var fromUser: FrontendUser = _

  @JsonProperty
  var toUser: FrontendUser = _

  @JsonProperty
  var title: String = _

  @JsonProperty
  var text: String = _

  @JsonProperty
  var creationDatetime: String = _
}
