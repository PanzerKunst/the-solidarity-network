package controllers.api

import services.JsonUtil
import play.api.mvc.{Action, Controller}
import database.{HelpResponseDto, HelpRequestDto}
import models.{HelpResponse, User, HelpRequest}
import controllers.Application
import play.api.Logger
import models.frontend.FrontendHelpRequest

object HelpResponseApi extends Controller {
  def create = Action(parse.json) {
    implicit request =>

      Application.loggedInUser(session) match {
        case Some(loggedInUser) => {
          val helpResponse = JsonUtil.deserialize[HelpResponse](request.body.toString)
          val helpResponseWithUserId = helpResponse.copy(responderId = loggedInUser.id)
          HelpResponseDto.create(helpResponseWithUserId)
          Ok
        }
        case None => {
          Logger.info("Help response creation attempt while not logged-in")
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
}
