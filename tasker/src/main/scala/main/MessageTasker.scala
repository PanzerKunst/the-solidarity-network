package main

import akka.actor.{Props, Actor}
import com.typesafe.scalalogging.slf4j.Logging
import db.{MessageQueries, NewAccountQueries}
import models.frontend.FrontendMessage
import models.TaskerMessage

object MessageTasker extends Logging {
  val actor = Tasker.system.actorOf(Props(new Actor {
    def receive = {
      case _ =>
        val messages = MessageQueries.selectNonProcessedMessages()

        for (message <- messages) {
          TaskerEmailService.sendMessageEmail(message)
          MessageQueries.updateLastProcessedMessage(message.id)
        }
    }
  }))
}
