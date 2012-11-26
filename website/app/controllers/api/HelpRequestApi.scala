package controllers.api

import services.JsonUtil
import play.api.mvc.{Action, Controller}
import database.{SubscriptionToHelpResponsesDto, HelpRequestDto}
import models.{SubscriptionToHelpResponses, User, HelpRequest}
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

      val queryString = request.queryString

      // Generic search
      val matchingHelpRequests: List[(HelpRequest, User)] = if (queryString.isEmpty)
        HelpRequestDto.searchGeneric(None)
      else if (queryString.contains("query"))
        HelpRequestDto.searchGeneric(Some(queryString.get("query").get.head))
      // Advanced search
      else {
        var filters: Map[String, String] = Map()
        filters = getUpdatedFiltersIfQueryStringContains(filters, queryString, "username")
        filters = getUpdatedFiltersIfQueryStringContains(filters, queryString, "firstName")
        filters = getUpdatedFiltersIfQueryStringContains(filters, queryString, "lastName")
        filters = getUpdatedFiltersIfQueryStringContains(filters, queryString, "city")
        filters = getUpdatedFiltersIfQueryStringContains(filters, queryString, "country")
        filters = getUpdatedFiltersIfQueryStringContains(filters, queryString, "respondedBy")
        HelpRequestDto.searchAdvanced(filters)
      }

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

          val filtersMap = Map("request_id" -> subscriptionWithSubscriberId.requestId.toString,
            "subscriber_id" -> subscriptionWithSubscriberId.subscriberId.get.toString)

          if (SubscriptionToHelpResponsesDto.get(Some(filtersMap)).isEmpty)
            SubscriptionToHelpResponsesDto.create(subscriptionWithSubscriberId) match {
              case Some(id) => Ok(id.toString)
              case None => InternalServerError("Creation of a subscription to help responses did not return an ID!")
            }
          else
            Ok
        }
        case None => {
          Logger.info("Attempt to create a subscription to help responses while not logged-in")
          Unauthorized
        }
      }
  }

  def unsubscribeFrom = Action(parse.json) {
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

  private def getUpdatedFiltersIfQueryStringContains(filters: Map[String, String], queryString: Map[String, Seq[String]], key: String): Map[String, String] = {
    if (queryString.contains(key)) {
      filters ++ Map(key -> queryString.get(key).get.head)
    }
    else filters
  }
}
