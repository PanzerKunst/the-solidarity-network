package models

import java.util.Date
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

case class HelpReply(
                      @JsonDeserialize(contentAs = classOf[java.lang.Long])
                      id: Option[Long] = None,

                      requestId: Long,

                      @JsonDeserialize(contentAs = classOf[java.lang.Long])
                      replierId: Option[Long] = None,

                      text: String,
                      creationDatetime: Option[Date] = None)