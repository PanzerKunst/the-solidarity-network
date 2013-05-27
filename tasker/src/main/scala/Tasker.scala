import akka.actor.ActorSystem
import com.typesafe.scalalogging.slf4j.Logging
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import scalikejdbc.config.DBs

object Tasker extends Logging {
  val system: ActorSystem = akka.actor.ActorSystem("system")

  def main(args: Array[String]) {
    DBs.setup()
    weeklyHelpRequestSubscriptions()
  }

  def weeklyHelpRequestSubscriptions() {
    //This will schedule to send the weeklyTick-message
    //to the tickActor after 0ms repeating every 7 days
    val cancellable = system.scheduler.schedule(
      0 milliseconds,
      7 days,
      HelpRequestSubscriptionTasker.tickActor,
      HelpRequestSubscriptionTasker.weeklyTick
    )
  }
}