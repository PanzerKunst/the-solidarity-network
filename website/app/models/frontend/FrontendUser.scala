package models.frontend

import models.{Country, User}
import database.CountryDto
import org.codehaus.jackson.annotate.JsonProperty

class FrontendUser {
  def this(user: User) = {
    this()

    this.id = user.id.get
    this.firstName = user.firstName.get
    this.lastName = user.lastName.get
    this.username = user.username
    this email = user.email.get
    this.streetAddress = user.streetAddress.get
    this.postCode = user.postCode.get
    this.city = user.city.get
    this.country = CountryDto.get(Some(Map("id" -> user.countryId.get.toString))).head
  }

  @JsonProperty
  var id: Long = _

  @JsonProperty
  var firstName: String = _

  @JsonProperty
  var lastName: String = _

  @JsonProperty
  var username: String = _

  @JsonProperty
  var email: String = _

  @JsonProperty
  var password: Option[String] = None

  @JsonProperty
  var streetAddress: String = _

  @JsonProperty
  var postCode: String = _

  @JsonProperty
  var city: String = _

  @JsonProperty
  var country: Country = _
}
