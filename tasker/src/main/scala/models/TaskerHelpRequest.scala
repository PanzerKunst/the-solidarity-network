package models

import org.codehaus.jackson.annotate.JsonProperty
import db.DbUtil

class TaskerHelpRequest {
  def this(helpRequest: HelpRequest, user: User, country: Country) = {

    this()

    this.id = helpRequest.id.get
    this.title = helpRequest.title
    this.description = helpRequest.description
    this.creationDatetime = DbUtil.datetimeToString(helpRequest.creationDatetime.get)
    this.expiryDate = DbUtil.dateToString(helpRequest.expiryDate)

    this.requester = new TaskerUser(
      user.id.get,
      user.firstName.get,
      user.lastName.get,
      user.username.get,
      user.email.get,
      user.streetAddress,
      user.postCode,
      user.city.get,
      country,
      user.description,
      user.isSubscribedToNews,
      user.subscriptionToNewHelpRequests
    )
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
  var requester: TaskerUser = _
}
