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
}
