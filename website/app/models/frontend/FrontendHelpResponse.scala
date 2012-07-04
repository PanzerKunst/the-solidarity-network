package models.frontend

import models.{HelpResponse, User}
import org.codehaus.jackson.annotate.JsonProperty
import database.UserDto

class FrontendHelpResponse extends HelpResponse {

  def this(helpResponse: HelpResponse) = {

    this()

    this.id = helpResponse.id
    this.requestId = helpResponse.requestId
    this.responderId = helpResponse.responderId
    this.text = helpResponse.text
    this.creationDatetime = helpResponse.creationDatetime

    this.responder = UserDto.get(Some(Map("id" -> this.responderId.toString))).head
  }

  @JsonProperty
  var responder: User = _
}
