package controllers.api

import services.JsonUtil
import play.api.mvc.{Action, Controller}
import database.{ReferenceDto, HelpRequestDto}
import models.{Reference, User, HelpRequest}
import controllers.Application
import play.api.Logger
import models.frontend.FrontendHelpRequest

object ReferenceApi extends Controller {
  def create = Action(parse.json) {
    implicit request =>

      Application.loggedInUser(session) match {
        case Some(loggedInUser) => {
          val reference = JsonUtil.parse(request.body.toString, classOf[Reference])
          val referenceWithUserId = reference.copy(fromUserId = loggedInUser.id)

          ReferenceDto.create(referenceWithUserId) match {
            case Some(id) => Ok(id.toString)
            case None => InternalServerError("Creation of a reference did not return an ID!")
          }
        }
        case None => {
          Logger.info("Reference creation attempt while not logged-in")
          Unauthorized
        }
      }
  }
}
