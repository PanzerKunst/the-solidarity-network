package models

case class SubscriptionToHelpReplies(id: Option[Long] = None,
                                     requestId: Long,
                                     subscriberId: Option[Long])
