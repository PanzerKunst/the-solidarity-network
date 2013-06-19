package main

import akka.actor.{Props, Actor}
import com.typesafe.scalalogging.slf4j.Logging
import db.HelpReplyQueries

object HelpReplySubscriptionTasker extends Logging {
  val actor = Tasker.system.actorOf(Props(new Actor {
    def receive = {
      case _ =>
        val nonProcessedHelpReplies = HelpReplyQueries.selectNonProcessedHelpReplies()

        for (reply <- nonProcessedHelpReplies) {
          for (subscriber <- HelpReplyQueries.selectSubscribersWillingToBeNotifiedOfReply(reply)) {
            TaskerEmailService.sendHelpReplySubscriptionEmail(subscriber, reply, reply.request)
          }

          HelpReplyQueries.updateLastProcessedReply(reply.id)
        }

    }
  }))
}
