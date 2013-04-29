package models.frontend

import models.{HelpReply, User}
import org.codehaus.jackson.annotate.JsonProperty
import database.{DbUtil, UserDto}

class FrontendHelpReply {

  def this(helpReply: HelpReply) = {
    this()

    this.id = helpReply.id.get
    this.requestId = helpReply.requestId
    this.text = helpReply.text
    this.creationDatetime = DbUtil.datetimeToString(helpReply.creationDatetime.get)

    this.replier = UserDto.get(Some(Map("id" -> helpReply.replierId.get.toString))).head
  }

  @JsonProperty
  var id: Long = _

  @JsonProperty
  var requestId: Long = _

  @JsonProperty
  var text: String = _

  @JsonProperty
  var creationDatetime: String = _

  @JsonProperty
  var replier: User = _
}
