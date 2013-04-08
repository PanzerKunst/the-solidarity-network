package models.frontend

import models.{HelpResponse, User}
import org.codehaus.jackson.annotate.JsonProperty
import database.{DbUtil, UserDto}
import java.util

class FrontendHelpResponse {

  def this(helpResponse: HelpResponse) = {
    this()

    this.id = helpResponse.id.get
    this.requestId = helpResponse.requestId
    this.text = helpResponse.text
    this.creationDatetime = DbUtil.datetimeToString(helpResponse.creationDatetime.get)

    this.responder = UserDto.get(Some(Map("id" -> helpResponse.responderId.get.toString))).head
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
  var responder: User = _
}
