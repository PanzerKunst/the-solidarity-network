package controllers.api

import services.JsonUtil
import play.api.mvc.{Action, Controller}
import database.{SubscriptionToHelpResponsesDto, HelpRequestDto}
import models.{SubscriptionToHelpResponses, User, Country, HelpRequest}
import controllers.Application
import play.api.Logger
import models.frontend.FrontendHelpRequest

object HelpRequestApi extends Controller {
  def create = Action(parse.json) {
    implicit request =>

      Application.loggedInUser(session) match {
        case Some(loggedInUser) => {
          val helpRequest = JsonUtil.parse(request.body.toString, classOf[HelpRequest])
          val helpRequestWithUserId = helpRequest.copy(requesterId = loggedInUser.id)

          HelpRequestDto.create(helpRequestWithUserId) match {
            case Some(id) => Ok(id.toString)
            case None => InternalServerError("Creation of a help request did not return an ID!")
          }
        }
        case None => {
          Logger.info("Help request creation attempt while not logged-in")
          Unauthorized
        }
      }
  }

  def get = Action {
    implicit request =>

      val query = if (request.queryString.contains("query"))
        Some(request.queryString.get("query").get.head)
      else
        None

      val matchingHelpRequests: List[(HelpRequest, User)] = HelpRequestDto.searchGeneric(query)

      if (matchingHelpRequests.isEmpty)
        NoContent
      else {
        val frontendHelpRequests = for (hr <- matchingHelpRequests) yield new FrontendHelpRequest(hr._1, hr._2)
        Ok(JsonUtil.serialize(frontendHelpRequests))
      }
  }

  def subscribeTo = Action(parse.json) {
    implicit request =>

      Application.loggedInUser(session) match {
        case Some(loggedInUser) => {
          val subscription = JsonUtil.parse(request.body.toString, classOf[SubscriptionToHelpResponses])
          val subscriptionWithSubscriberId = subscription.copy(subscriberId = loggedInUser.id)

          SubscriptionToHelpResponsesDto.create(subscriptionWithSubscriberId) match {
            case Some(id) => Ok(id.toString)
            case None => InternalServerError("Creation of a subscription to help responses did not return an ID!")
          }
        }
        case None => {
          Logger.info("Attempt to create a subscription to help responses while not logged-in")
          Unauthorized
        }
      }
  }

  def unsubscribeTo = Action(parse.json) {
    implicit request =>

      Application.loggedInUser(session) match {
        case Some(loggedInUser) => {
          val subscription = JsonUtil.parse(request.body.toString, classOf[SubscriptionToHelpResponses])
          val subscriptionWithSubscriberId = subscription.copy(subscriberId = loggedInUser.id)

          SubscriptionToHelpResponsesDto.delete(subscriptionWithSubscriberId)

          Ok
        }
        case None => {
          Logger.info("Attempt to delete a subscription to help responses while not logged-in")
          Unauthorized
        }
      }
  }
}
