package models.frontend

import models.{HelpResponse, User}
import org.codehaus.jackson.annotate.JsonProperty
import database.{DbUtil, UserDto}
import java.util

class FrontendHelpResponse {

  def this(helpResponse: HelpResponse) = {

    this()

    this.id = helpResponse.id
    this.requestId = helpResponse.requestId
    this.text = helpResponse.text
    this.creationDatetime = DbUtil.datetimeToString(helpResponse.creationDatetime)

    this.responder = UserDto.get(Some(Map("id" -> helpResponse.responderId.toString))).head
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
