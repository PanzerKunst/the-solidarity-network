package controllers.api

import models.User
import services.{EmailService, JsonUtil}
import database.UserDto
import play.api.mvc.{Action, Controller}
import play.api.Logger
import controllers.{FileController, Application}
import models.frontend.FrontendUser

object UserApi extends Controller {
  def create = Action(parse.json) {
    implicit request =>

      val userToCreate = JsonUtil.deserialize[User](request.body.toString)
      UserDto.create(userToCreate) match {
        case Some(id) =>
          val createdUser = UserDto.get(Some(Map("id" -> id.toString))).head
          EmailService.confirmRegistration(new FrontendUser(createdUser))
          Ok(id.toString)
        case None => InternalServerError("Creation of a user did not return an ID!")
      }
  }

  def update = Action(parse.json) {
    implicit request =>

      Application.loggedInUser(session) match {
        case Some(loggedInUser) =>
          val user = JsonUtil.deserialize[User](request.body.toString)
          val userWithId = user.copy(id = loggedInUser.id)
          val userWithPassword = updateUserWithCurrentPasswordIfNotChanged(userWithId, loggedInUser.password.get)

          UserDto.update(userWithPassword)
          FileController.saveTempProfilePicture(loggedInUser.id.get)
          Ok
        case None =>
          Logger.info("Profile update attempt while not logged-in")
          Unauthorized
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

      val matchingUsers = if (request.queryString.contains("notId")) {
        val exceptId = request.queryString.get("notId").get.head
        UserDto.getExceptOfId(filters, exceptId)
      } else
        UserDto.get(filters)


      if (matchingUsers.isEmpty)
        NoContent
      else
        Ok(JsonUtil.serialize(matchingUsers.head))
  }

  def get = Action {
    implicit request =>

      Application.loggedInUser(session) match {
        case Some(loggedInUser) =>

          val matchingUsers: List[User] = UserDto.searchExceptOfId(
            request.queryString.get("query").get.head,
            loggedInUser.id.get
          )

          if (matchingUsers.isEmpty)
            Ok(JsonUtil.serialize(List()))
          else {
            val frontendUsers = for (user <- matchingUsers) yield new FrontendUser(user)
            Ok(JsonUtil.serialize(frontendUsers))
          }
        case None => Unauthorized
      }
  }

  private def updateUserWithCurrentPasswordIfNotChanged(user: User, currentPassword: String) = {
    user.password match {
      case Some(password) => user
      case None => user.copy(password = Some(currentPassword))
    }
  }
}
