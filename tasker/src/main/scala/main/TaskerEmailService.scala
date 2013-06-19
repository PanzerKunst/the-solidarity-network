package main

import scala.concurrent.duration._
import com.typesafe.scalalogging.slf4j.Logging
import models._
import org.fusesource.scalate.TemplateEngine
import com.raulraja.services.{EmailMessage, EmailService, SmtpConfig}
import java.util.regex.Pattern
import com.raulraja.services.EmailMessage
import scala.Some
import models.HelpRequest
import com.raulraja.services.SmtpConfig
import models.frontend.FrontendMessage

object TaskerEmailService extends Logging {
  val engine = new TemplateEngine
  val regexPatternNewLine = Pattern.compile("\\n");

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

  def sendNewHelpRequestSubscriptionEmail(subscriber: TaskerUser, newHelpRequest: TaskerHelpRequest) {
    val matcherNewLine = regexPatternNewLine.matcher(newHelpRequest.description)
    newHelpRequest.description = matcherNewLine.replaceAll("<br />")

    val attr = Map(
      "subscriber" -> subscriber,
      "hr" -> newHelpRequest
    )

    EmailService.send(EmailMessage(
      subject = "New help request: '" + newHelpRequest.title + "'",
      recipient = subscriber.email,
      from = "Help Network Messenger <" + smtpConfig.user + ">",
      html = Some(engine.layout("emails/newHelpRequestSubscriptionTemplate.ssp", attr)),
      smtpConfig = smtpConfig,
      retryOn = 1 minute
    ))
  }

  def sendHelpReplySubscriptionEmail(subscriber: TaskerUser, newHelpReply: TaskerHelpReply, helpRequest: HelpRequest) {
    val matcherNewLine = regexPatternNewLine.matcher(newHelpReply.text)
    newHelpReply.text = matcherNewLine.replaceAll("<br />")

    val attr = Map(
      "subscriber" -> subscriber,
      "reply" -> newHelpReply,
      "hr" -> helpRequest
    )

    val emailContent = if (subscriber.id == newHelpReply.request.id.get) {
      (newHelpReply.replier.firstName.get + " replied to your help request",
        engine.layout("emails/newHelpReplyToOwnRequestTemplate.ssp", attr)
        )
    } else {
      (newHelpReply.replier.firstName.get + " replied to: '" + newHelpReply.request.title + "'",
        engine.layout("emails/newHelpReplySubscriptionTemplate.ssp", attr)
        )
    }

    logger.info("Sending email to '" + subscriber.email + "' with reply '" + newHelpReply.text)

    EmailService.send(EmailMessage(
      subject = emailContent._1,
      recipient = subscriber.email,
      from = "Help Network Messenger <" + smtpConfig.user + ">",
      html = Some(emailContent._2),
      smtpConfig = smtpConfig,
      retryOn = 1 minute
    ))
  }

  def sendNewAccountEmail(user: User) {
    val attr = Map(
      "user" -> user
    )

    EmailService.send(EmailMessage(
      subject = "Welcome to the Help Network",
      recipient = user.email.get,
      from = "Help Network Messenger <" + smtpConfig.user + ">",
      html = Some(engine.layout("emails/newAccountTemplate.ssp", attr)),
      smtpConfig = smtpConfig,
      retryOn = 1 minute
    ))
  }

  def sendMessageEmail(message: TaskerMessage) {
    val matcherNewLine = regexPatternNewLine.matcher(message.text)
    message.text = matcherNewLine.replaceAll("<br />")

    val attr = Map(
      "message" -> message
    )

    EmailService.send(EmailMessage(
      subject = "Msg from " + message.fromUser.username.get + ": " + message.title.get,
      recipient = message.toUser.email.get,
      from = "Help Network Messenger <" + smtpConfig.user + ">",
      html = Some(engine.layout("emails/messageTemplate.ssp", attr)),
      smtpConfig = smtpConfig,
      retryOn = 1 minute
    ))
  }

  private def sendPeriodicalHelpRequestSubscriptionEmail(subscriber: TaskerUser, newHelpRequests: List[TaskerHelpRequest], period: String) {
    for (hr <- newHelpRequests) {
      val matcherNewLine = regexPatternNewLine.matcher(hr.description)
      hr.description = matcherNewLine.replaceAll("<br />")
    }

    val attr = Map(
      "subscriber" -> subscriber,
      "helpRequests" -> newHelpRequests,
      "period" -> period.toLowerCase
    )

    EmailService.send(EmailMessage(
      subject = period + "'s new help requests",
      recipient = subscriber.email,
      from = "Help Network Messenger <" + smtpConfig.user + ">",
      html = Some(engine.layout("emails/periodicalHelpRequestSubscriptionTemplate.ssp", attr)),
      smtpConfig = smtpConfig,
      retryOn = 1 minute
    ))
  }
}
