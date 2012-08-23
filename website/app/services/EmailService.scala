package services

import views.html.emails.joinConfirmation
import com.typesafe.plugin._
import play.api.Play.current
import models.User

object EmailService {
  def sendJoinConfirmation(user: User) {
    val mail = use[MailerPlugin].email

    mail.addFrom("Help Network Support <support@thehelpnetwork.org>")
    mail.addRecipient(user.email.get)
    mail.setSubject("Registration confirmation")
    //send " " in text version otherwise it is not working. Bug reported by CBR to typesafe
    mail.send(" ", joinConfirmation(user).toString)
  }

}
