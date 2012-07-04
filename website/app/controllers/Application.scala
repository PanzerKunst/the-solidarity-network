package controllers

import play.api.mvc._
import models.User
import database.{HelpResponseDto, HelpRequestDto, UserDto, CountryDto}
import services.I18nService
import scala.collection.mutable
import models.frontend.{FrontendHelpResponse, FrontendHelpRequest}

object Application extends Controller {

  private val defaultLanguageCode = "en"

  def index = Action {
    Ok(views.html.index())
  }

  def join = Action {
    implicit request =>

      var i18n: mutable.Map[String, String] = null
      var languageCode = defaultLanguageCode

      if (request.queryString.contains("lang")) {
        languageCode = request.queryString.get("lang").get.head
        i18n = I18nService.get("join", languageCode)
      } else
        i18n = I18nService.get("join", defaultLanguageCode)

      Ok(views.html.join(i18n, CountryDto.get(None), languageCode))
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
          val frontendHelpResponses = for (helpResponse <- HelpResponseDto.get(Some(Map("request_id" -> id.toString)))) yield new FrontendHelpResponse(helpResponse)

          Ok(views.html.viewHelpRequest(user, new FrontendHelpRequest(helpRequest), frontendHelpResponses))
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