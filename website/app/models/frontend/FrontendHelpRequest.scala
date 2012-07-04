package models.frontend

import models.{Country, User, HelpRequest}
import org.codehaus.jackson.annotate.JsonProperty
import database.{CountryDto, UserDto}

class FrontendHelpRequest extends HelpRequest {

  def this(helpRequest: HelpRequest) = {

    this()

    this.id = helpRequest.id
    this.requesterId = helpRequest.requesterId
    this.title = helpRequest.title
    this.description = helpRequest.description
    this.creationDatetime = helpRequest.creationDatetime
    this.expiryDate = helpRequest.expiryDate

    this.requester = UserDto.get(Some(Map("id" -> this.requesterId.toString))).head
    this.country = CountryDto.get(Some(Map("id" -> this.requester.countryId.get.toString))).head
  }

  def this(helpRequest: HelpRequest,
           user: User,
           country: Country) = {

    this()

    this.id = helpRequest.id
    this.requesterId = helpRequest.requesterId
    this.title = helpRequest.title
    this.description = helpRequest.description
    this.creationDatetime = helpRequest.creationDatetime
    this.expiryDate = helpRequest.expiryDate

    this.requester = user
    this.country = country
  }

  @JsonProperty
  var requester: User = _

  @JsonProperty
  var country: Country = _

}
