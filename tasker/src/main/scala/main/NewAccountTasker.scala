package main

import akka.actor.{Props, Actor}
import com.typesafe.scalalogging.slf4j.Logging
import db.{NewAccountQueries, HelpReplyQueries}

object NewAccountTasker extends Logging {
  val actor = Tasker.system.actorOf(Props(new Actor {
    def receive = {
      case _ =>
        val newAccounts = NewAccountQueries.selectNonProcessedNewAccounts()

        for (user <- newAccounts) {
          TaskerEmailService.sendNewAccountEmail(user)
          NewAccountQueries.updateLastProcessedNewAccount(user.id.get)
        }

    }
  }))
}
