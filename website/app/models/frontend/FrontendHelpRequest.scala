package models.frontend

import models.{Country, User, HelpRequest}
import org.codehaus.jackson.annotate.JsonProperty
import database.{DbUtil, CountryDto, UserDto}
import java.util

class FrontendHelpRequest {

  def this(helpRequest: HelpRequest) = {

    this()

    this.id = helpRequest.id.get
    this.title = helpRequest.title
    this.description = helpRequest.description
    this.creationDatetime = DbUtil.datetimeToString(helpRequest.creationDatetime.get)
    this.expiryDate = DbUtil.dateToString(helpRequest.expiryDate)

    this.requester = UserDto.get(Some(Map("id" -> helpRequest.requesterId.get.toString))).head
    this.country = CountryDto.get(Some(Map("id" -> this.requester.countryId.get.toString))).head
  }

  def this(helpRequest: HelpRequest,
           user: User,
           country: Country) = {

    this()

    this.id = helpRequest.id.get
    this.title = helpRequest.title
    this.description = helpRequest.description
    this.creationDatetime = DbUtil.datetimeToString(helpRequest.creationDatetime.get)
    this.expiryDate = DbUtil.dateToString(helpRequest.expiryDate)

    this.requester = user
    this.country = country
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
  var requester: User = _

  @JsonProperty
  var country: Country = _

}
