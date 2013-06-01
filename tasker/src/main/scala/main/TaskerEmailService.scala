package main

import scala.concurrent.duration._
import com.typesafe.scalalogging.slf4j.Logging
import models.{TaskerHelpRequest, TaskerUser}
import org.fusesource.scalate.TemplateEngine
import com.raulraja.services.{EmailMessage, EmailService, SmtpConfig}

object TaskerEmailService extends Logging {
  val engine = new TemplateEngine

  val smtpConfig = SmtpConfig(
    host = "localhost",
    user = "noreply@himura.org",
    password = "tigrou"
  )

  def sendWeeklyHelpRequestSubscriptionEmail(subscriber: TaskerUser, newHelpRequests: List[TaskerHelpRequest]) {
    sendPeriodicalHelpRequestSubscriptionEmail(subscriber, newHelpRequests, "This week")
  }

  def sendDailyHelpRequestSubscriptionEmail(subscriber: TaskerUser, newHelpRequests: List[TaskerHelpRequest]) {
    sendPeriodicalHelpRequestSubscriptionEmail(subscriber, newHelpRequests, "Today")
  }

  private def sendPeriodicalHelpRequestSubscriptionEmail(subscriber: TaskerUser, newHelpRequests: List[TaskerHelpRequest], period: String) {
    val attr = Map(
      "subscriber" -> subscriber,
      "helpRequests" -> newHelpRequests,
      "period" -> period.toLowerCase
    )

    EmailService.send(EmailMessage(
      subject = period + "'s new help requests",
      recipient = subscriber.email,
      from = "The Help Network Notifier <" + smtpConfig.user + ">",
      html = Some(engine.layout("emails/periodicalHelpRequestSubscriptionTemplate.ssp", attr)),
      smtpConfig = smtpConfig,
      retryOn = 1 minute
    ))
  }
}
