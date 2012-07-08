package models

import java.util.Date

case class HelpResponse(id: Long,
                        requestId: Long,
                        responderId: Long,
                        text: String,
                        creationDatetime: Date)