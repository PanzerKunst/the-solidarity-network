package controllers.api

import services.JsonUtil
import play.api.mvc.{Action, Controller}
import database.{HelpReplyDto, HelpRequestDto}
import models.{HelpReply, User, HelpRequest}
import controllers.Application
import play.api.Logger
import models.frontend.FrontendHelpRequest

object HelpReplyApi extends Controller {
  def create = Action(parse.json) {
    implicit request =>

      Application.loggedInUser(session) match {
        case Some(loggedInUser) => {
          val helpReply = JsonUtil.deserialize[HelpReply](request.body.toString)
          val helpReplyWithUserId = helpReply.copy(replierId = loggedInUser.id)
          HelpReplyDto.create(helpReplyWithUserId)
          Ok
        }
        case None => {
          Logger.info("Help reply creation attempt while not logged-in")
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
