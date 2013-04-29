package models

import java.util.Date

case class HelpReply(id: Option[Long] = None,
                     requestId: Long,
                     replierId: Option[Long] = None,
                     text: String,
                     creationDatetime: Option[Date] = None)