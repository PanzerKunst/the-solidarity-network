package models

import java.util.Date

case class Reference(id: Option[Long] = None,
                     fromUserId: Option[Long] = None,
                     toUserId: Long,
                     wasHelped: Boolean,
                     ratingId: Long,
                     text: String,
                     creationDatetime: Option[Date])