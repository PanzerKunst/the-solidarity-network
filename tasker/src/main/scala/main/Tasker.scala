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

    weeklySubscriptionsToNewHelpRequests()
    dailySubscriptionsToNewHelpRequests()
    subscriptionsToEachNewHelpRequest()
    subscriptionsToHelpReplies()
    newAccount()
    message()
  }

  private def weeklySubscriptionsToNewHelpRequests() {
    //This will schedule to send the HelpRequestSubscriptionTasker.weeklyMsg
    //to the HelpRequestSubscriptionTasker.actor after 0ms repeating everyday
    val cancellable = system.scheduler.schedule(
      0 milliseconds,
      1 day,
      HelpRequestSubscriptionTasker.actor,
      HelpRequestSubscriptionTasker.weeklyMsg
    )
  }

  private def dailySubscriptionsToNewHelpRequests() {
    system.scheduler.schedule(
      0 milliseconds,
      1 hour,
      HelpRequestSubscriptionTasker.actor,
      HelpRequestSubscriptionTasker.dailyMsg
    )
  }

  private def subscriptionsToEachNewHelpRequest() {
    system.scheduler.schedule(
      0 milliseconds,
      1 minute,
      HelpRequestSubscriptionTasker.actor,
      HelpRequestSubscriptionTasker.eachNewRequestMsg
    )
  }

  private def subscriptionsToHelpReplies() {
    system.scheduler.schedule(
      0 milliseconds,
      10 seconds,
      HelpReplySubscriptionTasker.actor,
      None
    )
  }

  private def newAccount() {
    system.scheduler.schedule(
      0 milliseconds,
      10 seconds,
      NewAccountTasker.actor,
      None
    )
  }

  private def message() {
    system.scheduler.schedule(
      0 milliseconds,
      10 seconds,
      MessageTasker.actor,
      None
    )
  }
}