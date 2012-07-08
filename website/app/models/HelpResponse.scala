package models

import java.util.Date

case class HelpResponse(id: Option[Long] = None,
                        requestId: Long,
                        responderId: Option[Long] = None,
                        text: String,
                        creationDatetime: Option[Date] = None)