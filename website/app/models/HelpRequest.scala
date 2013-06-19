package models

import java.util.Date
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

case class HelpRequest(
                        @JsonDeserialize(contentAs = classOf[java.lang.Long])
                        id: Option[Long] = None,

                        @JsonDeserialize(contentAs = classOf[java.lang.Long])
                        requesterId: Option[Long] = None,

                        title: String,
                        description: String,
                        creationDatetime: Option[Date],
                        expiryDate: Date)