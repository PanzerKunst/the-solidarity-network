package models

import org.codehaus.jackson.annotate.JsonProperty
import java.util.Date


class HelpResponse {

  def this(id: Long,
           requestId: Long,
           responderId: Long,
           text: String,
           creationDate: Date) = {

    this()

    this.id = id
    this.responderId = responderId
    this.text = text
    this.creationDate = creationDate
  }

  @JsonProperty
  var id: Long = _

  @JsonProperty
  var requestId: Long = _

  @JsonProperty
  var responderId: Long = _

  @JsonProperty
  var text: String = _

  @JsonProperty
  var creationDate: Date = _
}