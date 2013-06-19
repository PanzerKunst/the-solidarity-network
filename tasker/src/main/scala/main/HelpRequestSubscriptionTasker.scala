package main

import akka.actor.{Props, Actor}
import com.typesafe.scalalogging.slf4j.Logging
import models._
import db.HelpRequestQueries
import java.util.{Calendar, GregorianCalendar}

object HelpRequestSubscriptionTasker extends Logging {
  val weeklyMsg = 0
  val dailyMsg = 1
  val eachNewRequestMsg = 2

  val actor = Tasker.system.actorOf(Props(new Actor {
    def receive = {
      case `weeklyMsg` ⇒
        if (new GregorianCalendar().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
          val nonProcessedHelpRequests = HelpRequestQueries.selectNonProcessedHelpRequests(User.NEW_HR_SUBSCRIPTION_FREQUENCY_WEEKLY)

          if (!nonProcessedHelpRequests.isEmpty) {
            for (subscriber <- HelpRequestQueries.selectSubscribers(User.NEW_HR_SUBSCRIPTION_FREQUENCY_WEEKLY)) {
              val helpRequestsInSameCity = nonProcessedHelpRequests.filter(hr => hr.requester.city == subscriber.city && hr.requester.country.id == subscriber.country.id)

              if (!helpRequestsInSameCity.isEmpty)
                TaskerEmailService.sendWeeklyHelpRequestSubscriptionEmail(subscriber, helpRequestsInSameCity)
            }

            HelpRequestQueries.updateLastProcessedRequest(User.NEW_HR_SUBSCRIPTION_FREQUENCY_WEEKLY, nonProcessedHelpRequests.last.id)
          }
        }

      case `dailyMsg` ⇒
        if (new GregorianCalendar().get(Calendar.HOUR_OF_DAY) == 0) {
          val nonProcessedHelpRequests = HelpRequestQueries.selectNonProcessedHelpRequests(User.NEW_HR_SUBSCRIPTION_FREQUENCY_DAILY)

          if (!nonProcessedHelpRequests.isEmpty) {
            for (subscriber <- HelpRequestQueries.selectSubscribers(User.NEW_HR_SUBSCRIPTION_FREQUENCY_DAILY)) {
              val helpRequestsInSameCity = nonProcessedHelpRequests.filter(hr => hr.requester.city == subscriber.city && hr.requester.country.id == subscriber.country.id)

              if (!helpRequestsInSameCity.isEmpty)
                TaskerEmailService.sendDailyHelpRequestSubscriptionEmail(subscriber, helpRequestsInSameCity)
            }

            HelpRequestQueries.updateLastProcessedRequest(User.NEW_HR_SUBSCRIPTION_FREQUENCY_DAILY, nonProcessedHelpRequests.last.id)
          }
        }

      case `eachNewRequestMsg` =>
        val nonProcessedHelpRequests = HelpRequestQueries.selectNonProcessedHelpRequests(User.NEW_HR_SUBSCRIPTION_FREQUENCY_EACH_NEW_REQUEST)

        for (hr <- nonProcessedHelpRequests) {
          for (subscriber <- HelpRequestQueries.selectSubscribersWillingToBeNotifiedOfNewRequest(hr)) {
            TaskerEmailService.sendNewHelpRequestSubscriptionEmail(subscriber, hr)
          }

          HelpRequestQueries.updateLastProcessedRequest(User.NEW_HR_SUBSCRIPTION_FREQUENCY_EACH_NEW_REQUEST, hr.id)
        }
    }
  }))
}
