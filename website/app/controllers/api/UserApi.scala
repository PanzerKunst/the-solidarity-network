package controllers.api

import models.User
import common.JsonUtil
import database.UserDto
import play.api.mvc.{AnyContentAsText, Action, Controller}
import controllers.Application

object UserApi extends Controller {
  def create = Action {
    implicit request =>

      Application.loggedInUser(session) match {
        case Some(user) => {
          val req: AnyContentAsText = request.body.asInstanceOf[AnyContentAsText]
          UserDto.create(JsonUtil.parse(req.txt, classOf[User]))
          Ok
        }
        case None => Unauthorized
      }
  }

  def getUsers(optionalUsername: Option[String]) = Action {
    implicit request =>

      val filters = optionalUsername match {
        case Some(username) => Some(Map("username" -> username))
        case None => None
      }

      UserDto.getAUserWhere(filters) match {
        case Some(user) => Ok(JsonUtil.serialize(user))
        case None => NotFound
      }
  }
}
