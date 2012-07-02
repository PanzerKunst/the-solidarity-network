package controllers

import play.api.mvc._
import models.User
import database.{HelpRequestDto, UserDto, CountryDto}
import services.I18nService
import scala.collection.mutable
import models.frontend.FrontendHelpRequest

object Application extends Controller {

  private val defaultLanguageCode = "en"

  def index = Action {
    Ok(views.html.index())
  }

  def register = Action {
    implicit request =>

      var i18n: mutable.Map[String, String] = null
      var languageCode = defaultLanguageCode

      if (request.queryString.contains("lang")) {
        languageCode = request.queryString.get("lang").get.head
        i18n = I18nService.get("register", languageCode)
      } else
        i18n = I18nService.get("register", defaultLanguageCode)

      Ok(views.html.register(i18n, CountryDto.get(None), languageCode))
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

  def viewHelpRequest(id: Int) = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(user) =>
          val helpRequest = HelpRequestDto.get(Some(Map("id" -> id.toString))).head
          Ok(views.html.viewHelpRequest(user, new FrontendHelpRequest(helpRequest)))
        case None => Redirect(routes.Application.login)
      }
  }

  def loggedInUser(session: Session): Option[User] = {
    session.get("userId") match {
      case Some(userId) => {
        val matchingUsers = UserDto.get(Some(Map("id" -> userId)))

        if (matchingUsers.isEmpty)
          None
        else
          Some(matchingUsers.head)
      }
      case None => None
    }
  }

}