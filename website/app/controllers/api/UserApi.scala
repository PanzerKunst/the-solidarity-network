package controllers.api

import models.User
import services.JsonUtil
import database.UserDto
import play.api.mvc.{Action, Controller}
import play.api.Logger
import controllers.Application

object UserApi extends Controller {
  def create = Action(parse.json) {
    implicit request =>

      UserDto.create(JsonUtil.parse(request.body.toString, classOf[User]))
      Ok
  }

  def update = Action(parse.json) {
    implicit request =>

      Application.loggedInUser(session) match {
        case Some(loggedInUser) => {
          val user = JsonUtil.parse(request.body.toString, classOf[User])
          val userWithId = user.copy(id = loggedInUser.id)
          val userWithPassword = updateUserWithCurrentPasswordIfNotChanged(userWithId, loggedInUser.password.get)

          UserDto.update(userWithPassword)
          Ok
        }
        case None => {
          Logger.info("Profile update attempt while not logged-in")
          Unauthorized
        }
      }
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
        NoContent
      else
        Ok(JsonUtil.serialize(matchingUsers.head))
  }

  private def updateUserWithCurrentPasswordIfNotChanged(user: User, password: String) = {
    user.password match {
      case Some(password) => user
      case None => user.copy(password = Some(password))
    }
  }
}
