package models

case class SubscriptionToHelpResponses(id: Option[Long] = None,
                                       requestId: Long,
                                       subscriberId: Option[Long])
