package models

import org.codehaus.jackson.annotate.JsonProperty
import db.DbUtil

class TaskerHelpReply {
  def this(helpReply: HelpReply, helpRequest: HelpRequest, user: User) = {

    this()

    this.id = helpReply.id.get
    this.text = helpReply.text
    this.creationDatetime = DbUtil.datetimeToString(helpReply.creationDatetime.get)
    this.request = helpRequest
    this.replier = user
  }

  @JsonProperty
  var id: Long = _

  @JsonProperty
  var request: HelpRequest = _

  @JsonProperty
  var text: String = _

  @JsonProperty
  var creationDatetime: String = _

  @JsonProperty
  var replier: User = _
}
