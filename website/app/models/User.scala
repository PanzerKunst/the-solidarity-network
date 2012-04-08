package models

import org.codehaus.jackson.annotate.JsonProperty


class User {

  def this(id: Long,
           username: String,
           firstName: String,
           lastName: String,
           email: String,
           password: String,
           streetAddress: String,
           postCode: String,
           city: String,
           countryId: Long) = {

    this()

    this.id = id
    this.username = username
    this.firstName = firstName
    this.lastName = lastName
    this.email = email
    this.password = password
    this.streetAddress = streetAddress
    this.postCode = postCode
    this.city = city
    this.countryId = countryId
  }

  @JsonProperty
  var id: Long = _

  @JsonProperty
  var username: String = _

  @JsonProperty
  var firstName: String = _

  @JsonProperty
  var lastName: String = _

  @JsonProperty
  var email: String = _

  @JsonProperty
  var password: String = _

  @JsonProperty
  var streetAddress: String = _

  @JsonProperty
  var postCode: String = _

  @JsonProperty
  var city: String = _

  @JsonProperty
  var countryId: Long = _

}