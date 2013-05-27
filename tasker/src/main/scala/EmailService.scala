import com.typesafe.scalalogging.slf4j.Logging
import models.frontend.{FrontendUser, FrontendHelpRequest}
import org.fusesource.scalate.TemplateEngine

object EmailService extends Logging {
  val engine = new TemplateEngine

  def sendWeeklyHelpRequestSubscriptionEmail(subscriber: FrontendUser, newHelpRequests: List[FrontendHelpRequest]) {
    val attr = Map(
      "subscriber" -> subscriber,
      "helpRequests" -> newHelpRequests
    )

    val emailContent = engine.layout("emails/weeklyHelpRequestSubscriptionTemplate.ssp", attr)

    logger.debug(emailContent)
  }
}
