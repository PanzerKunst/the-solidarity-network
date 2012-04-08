package controllers

import play.api.mvc._
import models.User
import database.{UserDto, CountryDto}

object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def register = Action {
    Ok(views.html.register(CountryDto.getAll))
  }

  def login = Action {
    Ok(views.html.login())
  }

  def home = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(user) => Ok(views.html.home(user))
        case None => Redirect(routes.Application.login)
      }
  }

  private def loggedInUser(session: Session): Option[User] = {
    session.get("userId") match {
      case Some(userId) => UserDto.getAUserWhere(Map("id" -> userId))
      case None => None
    }
  }

}