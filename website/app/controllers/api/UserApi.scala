package controllers.api

import models.User
import common.JsonUtil
import database.UserDto
import play.api.mvc.{AnyContentAsText, Action, Controller}

object UserApi extends Controller {
  def createUser = Action {
    request =>

      val req: AnyContentAsText = request.body.asInstanceOf[AnyContentAsText]
      UserDto.create(JsonUtil.parse(req.txt, classOf[User]))

      Ok
  }

  def getUsers(optionalUsername: Option[String]) = Action {
    request =>

      val filters = optionalUsername match {
        case Some(username) => Map("username" -> username)
      }

      UserDto.getAUserWhere(filters) match {
        case Some(user) => Ok(JsonUtil.serialize(user))
        case None => NotFound
      }
  }
}
