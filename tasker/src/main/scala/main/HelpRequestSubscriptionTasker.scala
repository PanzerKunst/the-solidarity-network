package main

import akka.actor.{Props, Actor}
import com.typesafe.scalalogging.slf4j.Logging
import models._
import db.TaskerQueries

object HelpRequestSubscriptionTasker extends Logging {
  val weeklyMsg = 0
  val dailyMsg = 1
  val eachRequestMsg = 2

  val actor = Tasker.system.actorOf(Props(new Actor {
    def receive = {
      case `weeklyMsg` ⇒
        val nonProcessedHelpRequests: List[TaskerHelpRequest] = TaskerQueries.selectNonProcessedHelpRequests(User.NEW_HR_SUBSCRIPTION_FREQUENCY_WEEKLY)

        if (!nonProcessedHelpRequests.isEmpty) {
          for (subscriber <- TaskerQueries.selectSubscribers(User.NEW_HR_SUBSCRIPTION_FREQUENCY_WEEKLY)) {
            val helpRequestsInSameCity = nonProcessedHelpRequests.filter(hr => hr.requester.city == subscriber.city && hr.requester.country.id == subscriber.country.id)
            TaskerEmailService.sendWeeklyHelpRequestSubscriptionEmail(subscriber, helpRequestsInSameCity)
          }

          TaskerQueries.updateLastProcessedRequest(User.NEW_HR_SUBSCRIPTION_FREQUENCY_WEEKLY, nonProcessedHelpRequests.last.id)
        }

      case `dailyMsg` ⇒
        val nonProcessedHelpRequests: List[TaskerHelpRequest] = TaskerQueries.selectNonProcessedHelpRequests(User.NEW_HR_SUBSCRIPTION_FREQUENCY_DAILY)

        if (!nonProcessedHelpRequests.isEmpty) {
          for (subscriber <- TaskerQueries.selectSubscribers(User.NEW_HR_SUBSCRIPTION_FREQUENCY_DAILY)) {
            val helpRequestsInSameCity = nonProcessedHelpRequests.filter(hr => hr.requester.city == subscriber.city && hr.requester.country.id == subscriber.country.id)
            TaskerEmailService.sendWeeklyHelpRequestSubscriptionEmail(subscriber, helpRequestsInSameCity)
          }

          TaskerQueries.updateLastProcessedRequest(User.NEW_HR_SUBSCRIPTION_FREQUENCY_DAILY, nonProcessedHelpRequests.last.id)
        }
    }
  }))
}
