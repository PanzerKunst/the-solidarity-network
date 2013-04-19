package services

import views.html.emails.confirmRegistrationTemplate
import views.html.emails.newMessageTemplate
import com.typesafe.plugin._
import play.api.Play.current
import models.frontend.{FrontendUser, FrontendMessage}

object EmailService {
  def confirmRegistration(user: FrontendUser) {
    val mail = use[MailerPlugin].email

    mail.addFrom("Help Network Support <support@thehelpnetwork.org>")
    mail.addRecipient(user.email)
    mail.setSubject("Registration confirmation")

    //send " " in text version otherwise it is not working. Bug reported to typesafe
    // TODO mail.send(" ", confirmRegistrationTemplate(user).toString())
  }

  def newMessage(message: FrontendMessage) {
    val mail = use[MailerPlugin].email

    mail.addFrom("Help Network Messenger <messenger@thehelpnetwork.org>")
    mail.addRecipient(message.toUser.email)
    mail.setSubject("Msg from " + message.fromUser.username + ": " + message.title)

    //send " " in text version otherwise it is not working. Bug reported to typesafe
    // TODO mail.send(" ", newMessageTemplate(message).toString())
  }
}
