package models

import org.codehaus.jackson.annotate.JsonProperty
import java.util.Date


class HelpRequest {

  def this(id: Long,
           requesterId: Long,
           title: String,
           description: String,
           creationDatetime: Date,
           expiryDate: Date) = {

    this()

    this.id = id
    this.requesterId = requesterId
    this.title = title
    this.description = description
    this.creationDatetime = creationDatetime
    this.expiryDate = expiryDate
  }

  @JsonProperty
  var id: Long = _

  @JsonProperty
  var title: String = _

  @JsonProperty
  var description: String = _

  @JsonProperty
  var requesterId: Long = _

  @JsonProperty
  var creationDatetime: Date = _

  @JsonProperty
  var expiryDate: Date = _

}