package models.frontend

import models.{Country, User}
import org.codehaus.jackson.annotate.JsonProperty
import db.CountryDto

class FrontendUser {
  def this(user: User) = {
    this()

    this.id = user.id.get
    this.firstName = user.firstName.get
    this.lastName = user.lastName.get
    this.username = user.username.get
    this.email = user.email.get
    this.streetAddress = user.streetAddress
    this.postCode = user.postCode
    this.city = user.city.get
    this.country = CountryDto.get(Some(Map("id" -> user.countryId.get.toString))).head
    this.description = user.description
    this.skillsAndInterests = user.skillsAndInterests
    this.isSubscribedToNews = user.isSubscribedToNews
    this.subscriptionToNewHelpRequests = user.subscriptionToNewHelpRequests
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
  var streetAddress: Option[String] = None

  @JsonProperty
  var postCode: Option[String] = None

  @JsonProperty
  var city: String = _

  @JsonProperty
  var country: Country = _

  @JsonProperty
  var description: Option[String] = None

  @JsonProperty
  var skillsAndInterests: Option[String] = None

  @JsonProperty
  var isSubscribedToNews: Boolean = _

  @JsonProperty
  var subscriptionToNewHelpRequests: String = _
}
