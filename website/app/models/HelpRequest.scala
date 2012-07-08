package models

import java.util.Date

case class HelpRequest(id: Option[Long] = None,
                       requesterId: Option[Long] = None,
                       title: String,
                       description: String,
                       creationDatetime: Option[Date],
                       expiryDate: Date)