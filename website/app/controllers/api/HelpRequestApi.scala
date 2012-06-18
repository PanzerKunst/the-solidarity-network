package controllers.api

import common.JsonUtil
import play.api.mvc.{AnyContentAsText, Action, Controller}
import database.HelpRequestDto
import models.HelpRequest
import controllers.Application
import play.api.Logger

object HelpRequestApi extends Controller {
  def create = Action {
    implicit request =>

      Application.loggedInUser(session) match {
        case Some(user) => {
          val req: AnyContentAsText = request.body.asInstanceOf[AnyContentAsText]
          val helpRequest = JsonUtil.parse(req.txt, classOf[HelpRequest])
          helpRequest.requesterId = user.id
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

      var filtersMap = Map[String, String]()

      val filters = if (filtersMap.size == 0)
        None
      else
        Some(filtersMap)

      val matchingHelpRequests = HelpRequestDto.get(filters)

      if (matchingHelpRequests.isEmpty)
        NotFound
      else
        Ok(JsonUtil.serialize(matchingHelpRequests))
  }
}
