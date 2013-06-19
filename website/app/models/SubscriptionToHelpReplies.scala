package models

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

case class SubscriptionToHelpReplies(
                                      @JsonDeserialize(contentAs = classOf[java.lang.Long])
                                      id: Option[Long] = None,

                                     requestId: Long,
                                     subscriberId: Option[Long])
