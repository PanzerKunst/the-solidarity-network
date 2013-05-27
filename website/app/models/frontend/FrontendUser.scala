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
    this.username = user.username.get
    this.email = user.email.get
    this.streetAddress = user.streetAddress
    this.postCode = user.postCode
    this.city = user.city.get
    this.country = CountryDto.get(Some(Map("id" -> user.countryId.get.toString))).head
    this.description = user.description
    this.isSubscribedToNews = user.isSubscribedToNews
    this.subscriptionToNewHelpRequests = user.subscriptionToNewHelpRequests
  }

  def this(id: Long,
           firstName: String,
           lastName: String,
           username: String,
           email: String,
           city: String,
           country: Country,
           isSubscribedToNews: Boolean,
           subscriptionToNewHelpRequests: String) = {
    this()

    this.id = id
    this.firstName = firstName
    this.lastName = lastName
    this.username = username
    this.email = email
    this.city = city
    this.country = country
    this.isSubscribedToNews = isSubscribedToNews
    this.subscriptionToNewHelpRequests = subscriptionToNewHelpRequests
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
  var isSubscribedToNews: Boolean = _

  @JsonProperty
  var subscriptionToNewHelpRequests: String = _
}
