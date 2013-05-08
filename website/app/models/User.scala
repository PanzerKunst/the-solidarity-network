package models

case class User(id: Option[Long] = None,
                firstName: Option[String] = None,
                lastName: Option[String] = None,
                username: Option[String] = None,
                email: Option[String] = None,
                password: Option[String] = None,
                streetAddress: Option[String] = None,
                postCode: Option[String] = None,
                city: Option[String] = None,
                countryId: Option[Long] = None,
                description: Option[String] = None,
                isSubscribedToNews: Boolean,
                subscriptionToNewHelpRequests: String)
