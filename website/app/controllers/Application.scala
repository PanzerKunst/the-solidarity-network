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

  def logout = Action {
    Redirect(routes.Application.index).withNewSession
  }

  def home = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(user) => Ok(views.html.home(user))
        case None => Redirect(routes.Application.login)
      }
  }

  def myProfile = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(user) => Ok(views.html.myProfile(user))
        case None => Redirect(routes.Application.login)
      }
  }

  def createHelpRequest = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(user) => Ok(views.html.createHelpRequest(user))
        case None => Redirect(routes.Application.login)
      }
  }

  def searchHelpRequests = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(user) => Ok(views.html.searchHelpRequests(user))
        case None => Redirect(routes.Application.login)
      }
  }

  def helpDashboard = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(user) => Ok(views.html.helpDashboard(user))
        case None => Redirect(routes.Application.login)
      }
  }

  def loggedInUser(session: Session): Option[User] = {
    session.get("userId") match {
      case Some(userId) => UserDto.getAUserWhere(Some(Map("id" -> userId)))
      case None => None
    }
  }

}