package controllers.api

import models.User
import common.JsonUtil
import database.UserDto
import play.api.mvc.{Action, Controller}

object UserApi extends Controller {
  def create = Action(parse.json) {
    implicit request =>

      UserDto.create(JsonUtil.parse(request.body.toString, classOf[User]))
      Ok
  }

  def getFirst = Action {
    implicit request =>

      var filtersMap = Map[String, String]()

      if (request.queryString.contains("username")) {
        val username = request.queryString.get("username").get.head
        filtersMap += ("username" -> username)
      }

      if (request.queryString.contains("email")) {
        val email = request.queryString.get("email").get.head
        filtersMap += ("email" -> email)
      }

      val filters = if (filtersMap.size == 0)
        None
      else
        Some(filtersMap)

      val matchingUsers = UserDto.get(filters)

      if (matchingUsers.isEmpty)
        NotFound
      else
        Ok(JsonUtil.serialize(matchingUsers.head))
  }
}
