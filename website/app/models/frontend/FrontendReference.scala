package models.frontend

import models.{ReferenceRating, Reference, User}
import db.{DbUtil, ReferenceRatingDto, UserDto}
import org.codehaus.jackson.annotate.JsonProperty

class FrontendReference {
  def this(reference: Reference) = {
    this()

    this.id = reference.id.get
    this.fromUser = new FrontendUser(UserDto.get(Some(Map("id" -> reference.fromUserId.get.toString))).head)
    this.toUser = new FrontendUser(UserDto.get(Some(Map("id" -> reference.toUserId.toString))).head)
    this.wasHelped = reference.wasHelped
    this.rating = ReferenceRatingDto.get(Some(Map("id" -> reference.ratingId.toString))).head
    this.text = reference.text
    this.creationDatetime = DbUtil.datetimeToString(reference.creationDatetime.get)
  }

  def this(reference: Reference, toUser: User) = {
    this()

    this.id = reference.id.get
    this.fromUser = new FrontendUser(UserDto.get(Some(Map("id" -> reference.fromUserId.get.toString))).head)
    this.toUser = new FrontendUser(toUser)
    this.wasHelped = reference.wasHelped
    this.rating = ReferenceRatingDto.get(Some(Map("id" -> reference.ratingId.toString))).head
    this.text = reference.text
    this.creationDatetime = DbUtil.datetimeToString(reference.creationDatetime.get)
  }

  @JsonProperty
  var id: Long = _

  @JsonProperty
  var fromUser: FrontendUser = _

  @JsonProperty
  var toUser: FrontendUser = _

  @JsonProperty
  var wasHelped: Boolean = true

  @JsonProperty
  var rating: ReferenceRating = _

  @JsonProperty
  var text: String = _

  @JsonProperty
  var creationDatetime: String = _
}
