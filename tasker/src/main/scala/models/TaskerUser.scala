package models

import org.codehaus.jackson.annotate.JsonProperty

class TaskerUser {
  def this(id: Long,
           firstName: String,
           lastName: String,
           username: String,
           email: String,
           streetAddress: Option[String],
           postCode: Option[String],
           city: String,
           country: Country,
           description: Option[String],
           isSubscribedToNews: Boolean,
           subscriptionToNewHelpRequests: String) = {
    this()

    this.id = id
    this.firstName = firstName
    this.lastName = lastName
    this.username = username
    this.email = email
    this.streetAddress = streetAddress
    this.postCode = postCode
    this.city = city
    this.country = country
    this.description = description
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
