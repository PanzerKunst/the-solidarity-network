package controllers.api

import common.JsonUtil
import database.UserDto
import play.api.mvc.{AnyContentAsText, Action, Controller}
import models.User

object AuthApi extends Controller {
  def authenticate = Action {
    implicit request =>

      val req: AnyContentAsText = request.body.asInstanceOf[AnyContentAsText]
      val user = JsonUtil.parse(req.txt, classOf[User])

      var filtersMap = Map[String, String]()

      if (user.username != null)
        filtersMap += ("username" -> user.username)
      else if (user.email != null)
        filtersMap += ("email" -> user.email)

      filtersMap += ("password" -> user.password)

      UserDto.getAUserWhere(Some(filtersMap)) match {
        case Some(user) => Ok.withSession(
          session + ("userId" -> user.id.toString)
        )
        case None => NotFound
      }
  }
}
