package models

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

case class User(
                 @JsonDeserialize(contentAs = classOf[java.lang.Long])
                 id: Option[Long] = None,

                 firstName: Option[String] = None,
                 lastName: Option[String] = None,
                 username: Option[String] = None,
                 email: Option[String] = None,
                 password: Option[String] = None,
                 streetAddress: Option[String] = None,
                 postCode: Option[String] = None,
                 city: Option[String] = None,

                 @JsonDeserialize(contentAs = classOf[java.lang.Long])
                 countryId: Option[Long] = None,

                 description: Option[String] = None,
                 isSubscribedToNews: Boolean,
                 subscriptionToNewHelpRequests: String)

object User {
  val NEW_HR_SUBSCRIPTION_FREQUENCY_NONE = "NONE"
  val NEW_HR_SUBSCRIPTION_FREQUENCY_EACH_NEW_REQUEST = "EACH_NEW_REQUEST"
  val NEW_HR_SUBSCRIPTION_FREQUENCY_DAILY = "DAILY"
  val NEW_HR_SUBSCRIPTION_FREQUENCY_WEEKLY = "WEEKLY"
}