package models

import org.codehaus.jackson.annotate.JsonProperty
import java.util.Date


class HelpRequest {

  def this(id: Long,
           description: String,
           requesterId: Long,
           expiryDate: Date) = {

    this()

    this.id = id
    this.description = description
    this.requesterId = requesterId
    this.expiryDate = expiryDate
  }

  @JsonProperty
  var id: Long = _

  @JsonProperty
  var description: String = _

  @JsonProperty
  var requesterId: Long = _

  @JsonProperty
  var expiryDate: Date = _

}