package main

import akka.actor.ActorSystem
import com.typesafe.scalalogging.slf4j.Logging
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import scalikejdbc.config.DBs

object Tasker extends Logging {
  val system = ActorSystem("TaskerSystem")

  def main(args: Array[String]) {
    DBs.setup()
    weeklyHelpRequestSubscriptions()
    dailyHelpRequestSubscriptions()
  }

  private def weeklyHelpRequestSubscriptions() {
    //This will schedule to send the HelpRequestSubscriptionTasker.weeklyMsg
    //to the HelpRequestSubscriptionTasker.actor after 0ms repeating every 7 days
    val cancellable = system.scheduler.schedule(
      0 milliseconds,
      70 milliseconds,
      HelpRequestSubscriptionTasker.actor,
      HelpRequestSubscriptionTasker.weeklyMsg
    )
  }

  private def dailyHelpRequestSubscriptions() {
    val cancellable = system.scheduler.schedule(
      0 milliseconds,
      100 milliseconds,
      HelpRequestSubscriptionTasker.actor,
      HelpRequestSubscriptionTasker.dailyMsg
    )
  }
}