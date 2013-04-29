package controllers.api

import services.JsonUtil
import play.api.mvc.{Action, Controller}
import database.{SubscriptionToHelpRepliesDto, HelpRequestDto}
import models.{SubscriptionToHelpReplies, User, HelpRequest}
import controllers.Application
import play.api.Logger
import models.frontend.FrontendHelpRequest

object HelpRequestApi extends Controller {
  def create = Action(parse.json) {
    implicit request =>

      Application.loggedInUser(session) match {
        case Some(loggedInUser) => {
          val helpRequest = JsonUtil.deserialize[HelpRequest](request.body.toString)
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

  def update = Action(parse.json) {
    implicit request =>

      Application.loggedInUser(session) match {
        case Some(loggedInUser) => {
          val helpRequest = JsonUtil.deserialize[HelpRequest](request.body.toString)

          if (helpRequest.requesterId.get == loggedInUser.id.get) {
            HelpRequestDto.update(helpRequest)
            Ok
          }
          else
            Forbidden("Only your own help requests, you may update.")
        }
        case None => {
          Logger.info("Help request update attempt while not logged-in")
          Unauthorized
        }
      }
  }

  def delete = Action(parse.json) {
    implicit request =>

      Application.loggedInUser(session) match {
        case Some(loggedInUser) => {
          val helpRequest = JsonUtil.deserialize[HelpRequest](request.body.toString)

          val subscriptions = SubscriptionToHelpRepliesDto.get(Some(Map("request_id" -> helpRequest.id.get.toString)))
          for (subscription <- subscriptions)
            SubscriptionToHelpRepliesDto.delete(subscription)

          if (helpRequest.requesterId.get == loggedInUser.id.get) {
            HelpRequestDto.delete(helpRequest)
            Ok
          }
          else
            Forbidden("Only your own help requests, you may delete.")
        }
        case None => {
          Logger.info("Help request delete attempt while not logged-in")
          Unauthorized
        }
      }
  }

  def get = Action {
    implicit request =>
      Application.loggedInUser(session) match {
        case Some(loggedInUser) => {

          val queryString = request.queryString

          val exceptForRequesterId = queryString.get("isOwnFiltered") match {
            case Some(isOwnFiltered) =>
              if (isOwnFiltered.head.toBoolean)
                loggedInUser.id
              else
                None
            case None => None
          }

          // Generic search
          val matchingHelpRequests: List[(HelpRequest, User)] = if (queryString.isEmpty)
            HelpRequestDto.searchGeneric(None, exceptForRequesterId)
          else if (queryString.contains("query"))
            HelpRequestDto.searchGeneric(Some(queryString.get("query").get.head), exceptForRequesterId)
          // Advanced search
          else {
            var filters: Map[String, String] = Map()
            filters = getUpdatedFiltersIfQueryStringContains(filters, queryString, "username")
            filters = getUpdatedFiltersIfQueryStringContains(filters, queryString, "firstName")
            filters = getUpdatedFiltersIfQueryStringContains(filters, queryString, "lastName")
            filters = getUpdatedFiltersIfQueryStringContains(filters, queryString, "city")
            filters = getUpdatedFiltersIfQueryStringContains(filters, queryString, "country")
            filters = getUpdatedFiltersIfQueryStringContains(filters, queryString, "repliedBy")
            HelpRequestDto.searchAdvanced(filters, exceptForRequesterId)
          }

          if (matchingHelpRequests.isEmpty)
            NoContent
          else {
            val frontendHelpRequests = for (hr <- matchingHelpRequests) yield new FrontendHelpRequest(hr._1, hr._2)
            Ok(JsonUtil.serialize(frontendHelpRequests))
          }
        }
        case None => {
          Logger.info("Help request search attempt while not logged-in")
          Unauthorized
        }
      }
  }

  def subscribeTo = Action(parse.json) {
    implicit request =>

      Application.loggedInUser(session) match {
        case Some(loggedInUser) => {
          val subscription = JsonUtil.deserialize[SubscriptionToHelpReplies](request.body.toString)
          val subscriptionWithSubscriberId = subscription.copy(subscriberId = loggedInUser.id)

          val filtersMap = Map("request_id" -> subscriptionWithSubscriberId.requestId.toString,
            "subscriber_id" -> subscriptionWithSubscriberId.subscriberId.get.toString)

          if (SubscriptionToHelpRepliesDto.get(Some(filtersMap)).isEmpty)
            SubscriptionToHelpRepliesDto.create(subscriptionWithSubscriberId) match {
              case Some(id) => Ok(id.toString)
              case None => InternalServerError("Creation of a subscription to help replies did not return an ID!")
            }
          else
            Ok
        }
        case None => {
          Logger.info("Attempt to create a subscription to help replies while not logged-in")
          Unauthorized
        }
      }
  }

  def unsubscribeFrom = Action(parse.json) {
    implicit request =>

      Application.loggedInUser(session) match {
        case Some(loggedInUser) => {
          val subscription = JsonUtil.deserialize[SubscriptionToHelpReplies](request.body.toString)
          val subscriptionWithSubscriberId = subscription.copy(subscriberId = loggedInUser.id)

          SubscriptionToHelpRepliesDto.delete(subscriptionWithSubscriberId)

          Ok
        }
        case None => {
          Logger.info("Attempt to delete a subscription to help replies while not logged-in")
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
