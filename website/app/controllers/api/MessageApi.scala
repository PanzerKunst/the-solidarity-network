package controllers.api

import models.Message
import services.{EmailService, JsonUtil}
import database.MessageDto
import play.api.mvc.{Action, Controller}
import play.api.Logger
import controllers.Application
import models.frontend.FrontendMessage

object MessageApi extends Controller {
  def create = Action(parse.json) {
    implicit request =>

      Application.loggedInUser(session) match {
        case Some(loggedInUser) => {
          val msg = JsonUtil.deserialize[Message](request.body.toString)
          val msgWithUserId = msg.copy(fromUserId = loggedInUser.id)
          MessageDto.create(msgWithUserId) match {
            case Some(id) =>
              val createdMessage = MessageDto.get(Map("id" -> id.toString)).head
              EmailService.newMessage(new FrontendMessage(createdMessage))
              Ok(id.toString)
            case None => InternalServerError("Creation of a reference did not return an ID!")
          }
        }
        case None => {
          Logger.info("Message creation attempt while not logged-in")
          Unauthorized
        }
      }
  }
}
