package models.frontend

import models.{User, HelpRequest}
import org.codehaus.jackson.annotate.JsonProperty
import database.UserDto

class FrontendHelpRequest extends HelpRequest {

  def this(helpRequest: HelpRequest) = {

    this()

    this.id = helpRequest.id
    this.requesterId = helpRequest.requesterId
    this.title = helpRequest.title
    this.description = helpRequest.description
    this.creationDate = helpRequest.creationDate
    this.expiryDate = helpRequest.expiryDate

    this.requester = UserDto.get(Some(Map("id" -> this.requesterId.toString))).head
  }

  @JsonProperty
  var requester: User = _

}
