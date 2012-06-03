package controllers.api

import common.JsonUtil
import database.UserDto
import play.api.mvc.{AnyContentAsText, Action, Controller}
import collection.mutable.HashMap
import models.User

object AuthApi extends Controller {
  def authenticate = Action {
    implicit request =>

      val req: AnyContentAsText = request.body.asInstanceOf[AnyContentAsText]
      val user = JsonUtil.parse(req.txt, classOf[User])

      val filters = new HashMap[String, String]
      filters += "password" -> user.password
      if (user.username != null)
        filters += "username" -> user.username
      else if (user.email != null)
        filters += "email" -> user.email

      UserDto.getAUserWhere(Some(filters toMap)) match {
        case Some(user) => Ok.withSession(
          session + ("userId" -> user.id.toString)
        )
        case None => NotFound
      }
  }

}
