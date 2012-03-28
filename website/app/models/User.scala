package models

import org.codehaus.jackson.map.BeanProperty

case class User(username: String,
                firstName: String,
                lastName: String,
                email: String,
                password: String,
                streetAddress: String,
                postCode: String,
                city: String,
                countryId: Long) {

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

    this(username,
      firstName,
      lastName,
      email,
      password,
      streetAddress,
      postCode,
      city,
      countryId)

    this.id = id
  }

  var id: Long = _
  
  def getId = { id }
}