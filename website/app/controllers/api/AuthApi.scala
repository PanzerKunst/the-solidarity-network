package controllers.api

import services.JsonUtil
import play.api.mvc.{Action, Controller}
import models.User
import db.UserDto

object AuthApi extends Controller {
  def authenticate = Action(parse.json) {
    implicit request =>

      val user = JsonUtil.deserialize[User](request.body.toString)

      var filtersMap = Map[String, String]()

      user.username match {
        case Some(username) => filtersMap += ("username" -> username)
        case None => user.email match {
          case Some(email) => filtersMap += ("email" -> email)
          case None =>
        }
      }

      filtersMap += ("password" -> user.password.get)

      val matchingUsers = UserDto.get(Some(filtersMap))

      if (matchingUsers.isEmpty)
        NoContent
      else
        Ok.withSession(
          session + ("userId" -> matchingUsers.head.id.get.toString)
        )
  }
}
