package service

import play.libs.Mail
import org.apache.commons.mail.HtmlEmail
import views.Mails._

object EmailService {
  def sendSignupForPreviewConfirmation() {
    val email = new HtmlEmail()

    email.setFrom("appeariq@appearnetworks.com", "Appear IQ")
    email.addTo("signupForPreview.email")
    email.setSubject("Appear IQ BETA program sign-up confirmation")
    email.setHtmlMsg(html.signupForPreviewConfirmationEmail().toString())

    Mail.send(email)
  }
}
