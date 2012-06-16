package models

import org.codehaus.jackson.annotate.JsonProperty
import java.util.Date


class HelpRequest {

  def this(id: Long,
           requesterId: Long,
           title: String,
           description: String,
           creationDate: Date,
           expiryDate: Date) = {

    this()

    this.id = id
    this.requesterId = requesterId
    this.title = title
    this.description = description
    this.creationDate = creationDate
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
  var creationDate: Date = _

  @JsonProperty
  var expiryDate: Date = _

}