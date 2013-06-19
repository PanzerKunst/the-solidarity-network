package models

import java.util.Date
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

case class Reference(
                      @JsonDeserialize(contentAs = classOf[java.lang.Long])
                      id: Option[Long] = None,

                      @JsonDeserialize(contentAs = classOf[java.lang.Long])
                      fromUserId: Option[Long] = None,

                      toUserId: Long,
                      wasHelped: Boolean,
                      ratingId: Long,
                      text: String,
                      creationDatetime: Option[Date])