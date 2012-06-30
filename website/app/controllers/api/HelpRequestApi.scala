package controllers.api

import services.JsonUtil
import play.api.mvc.{Action, Controller}
import database.HelpRequestDto
import models.{User, Country, HelpRequest}
import controllers.Application
import play.api.Logger
import models.frontend.FrontendHelpRequest

object HelpRequestApi extends Controller {
  def create = Action(parse.json) {
    implicit request =>

      Application.loggedInUser(session) match {
        case Some(user) => {
          val helpRequest = JsonUtil.parse(request.body.toString, classOf[HelpRequest])
          helpRequest.requesterId = user.id.get
          HelpRequestDto.create(helpRequest)
          Ok
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

      val matchingHelpRequests: List[(HelpRequest, User, Country)] = HelpRequestDto.searchGeneric(query)

      if (matchingHelpRequests.isEmpty)
        NoContent
      else {
        val frontendHelpRequests = for (hr <- matchingHelpRequests) yield new FrontendHelpRequest(hr._1, hr._2, hr._3)
        Ok(JsonUtil.serialize(frontendHelpRequests))
      }
  }
}
