package controllers.api

import common.JsonUtil
import database.UserDto
import play.api.mvc.{Action, Controller}
import models.User

object AuthApi extends Controller {
  def authenticate = Action(parse.json) {
    implicit request =>

      val user = JsonUtil.parse(request.body.toString, classOf[User])

      var filtersMap = Map[String, String]()

      if (user.username != null)
        filtersMap += ("username" -> user.username)
      else if (user.email != null)
        filtersMap += ("email" -> user.email)

      filtersMap += ("password" -> user.password)

      val matchingUsers = UserDto.get(Some(filtersMap))

      if (matchingUsers.isEmpty)
        NotFound
      else
        Ok.withSession(
          session + ("userId" -> matchingUsers.head.id.toString)
        )
  }
}
