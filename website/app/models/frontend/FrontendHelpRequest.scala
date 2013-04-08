package models.frontend

import models.{User, HelpRequest}
import org.codehaus.jackson.annotate.JsonProperty
import database.{DbUtil, UserDto}

class FrontendHelpRequest {

  def this(helpRequest: HelpRequest) = {
    this()

    this.id = helpRequest.id.get
    this.title = helpRequest.title
    this.description = helpRequest.description
    this.creationDatetime = DbUtil.datetimeToString(helpRequest.creationDatetime.get)
    this.expiryDate = DbUtil.dateToString(helpRequest.expiryDate)

    this.requester = new FrontendUser(UserDto.get(Some(Map("id" -> helpRequest.requesterId.get.toString))).head)
  }

  def this(helpRequest: HelpRequest, user: User) = {

    this()

    this.id = helpRequest.id.get
    this.title = helpRequest.title
    this.description = helpRequest.description
    this.creationDatetime = DbUtil.datetimeToString(helpRequest.creationDatetime.get)
    this.expiryDate = DbUtil.dateToString(helpRequest.expiryDate)

    this.requester = new FrontendUser(user)
  }

  @JsonProperty
  var id: Long = _

  @JsonProperty
  var title: String = _

  @JsonProperty
  var description: String = _

  @JsonProperty
  var creationDatetime: String = _

  @JsonProperty
  var expiryDate: String = _

  @JsonProperty
  var requester: FrontendUser = _

}
