package controllers

import play.api.mvc._
import database._
import services.I18nService
import scala.collection.mutable
import models.frontend.{FrontendReference, FrontendUser, FrontendHelpResponse, FrontendHelpRequest}
import models.User
import scala.Some

object Application extends Controller {

  private val defaultLanguageCode = "en"

  def index = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(loggedInUser) => Redirect(routes.Application.home)
        case None => Ok(views.html.index())
      }
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
    implicit request =>
      val from = if (request.queryString.contains("from"))
        Some(request.queryString.get("from").get.head)
      else
        None

      val to = if (request.queryString.contains("to"))
        Some(request.queryString.get("to").get.head)
      else
        None

      val username = if (request.queryString.contains("username"))
        Some(request.queryString.get("username").get.head)
      else
        None

      Ok(views.html.login(username, from, to))
  }

  def logout = Action {
    Redirect(routes.Application.index).withNewSession
  }

  def home = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(loggedInUser) => Ok(views.html.home(loggedInUser))
        case None => Redirect(routes.Application.login)
      }
  }

  def myProfile = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(loggedInUser) =>
          val references = ReferenceDto.get(Some(Map("to_user_id" -> loggedInUser.id.get.toString)))
          val frontendReferences = for (ref <- references) yield new FrontendReference(ref, loggedInUser)
          Ok(views.html.profile(loggedInUser, new FrontendUser(loggedInUser), frontendReferences))
        case None => Redirect(routes.Application.login)
      }
  }

  def editProfile = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(loggedInUser) => Ok(views.html.editProfile(CountryDto.get(None), loggedInUser, new FrontendUser(loggedInUser)))
        case None => Redirect(routes.Application.login)
      }
  }

  def profile(username: String) = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(loggedInUser) =>
          val user = UserDto.get(Some(Map("username" -> username))).head
          val references = ReferenceDto.get(Some(Map("to_user_id" -> user.id.get.toString)))
          val frontendReferences = for (ref <- references) yield new FrontendReference(ref, user)
          Ok(views.html.profile(loggedInUser, new FrontendUser(user), frontendReferences))
        case None => Redirect(routes.Application.login)
      }
  }

  def createHelpRequest = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(loggedInUser) => Ok(views.html.createHelpRequest(loggedInUser))
        case None => Redirect(routes.Application.login)
      }
  }

  def searchHelpRequests = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(loggedInUser) =>

          var query: Map[String, String] = Map()

          for (key <- request.queryString.keys) {
            query += (key -> request.queryString.get(key).get.head)
          }

          Ok(views.html.searchHelpRequests(loggedInUser, query))
        case None => Redirect(routes.Application.login)
      }
  }

  def helpDashboard = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(loggedInUser) =>
          val from = if (request.queryString.contains("from"))
            Some(request.queryString.get("from").get.head)
          else
            None

          Ok(views.html.helpDashboard(loggedInUser, new FrontendUser(loggedInUser), from))
        case None => Redirect(routes.Application.login)
      }
  }

  def viewHelpRequest(id: Int) = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(loggedInUser) =>
          val helpRequests = HelpRequestDto.get(Some(Map("id" -> id.toString)))

          if (helpRequests.isEmpty) {
            NotFound("Help request not found")
          }
          else {
            val filtersMap = Map("request_id" -> id.toString,
              "subscriber_id" -> loggedInUser.id.get.toString)
            val isSubscribedToResponses = !SubscriptionToHelpResponsesDto.get(Some(filtersMap)).isEmpty

            val frontendHelpResponses = for (helpResponse <- HelpResponseDto.get(Some(Map("request_id" -> id.toString)))) yield new FrontendHelpResponse(helpResponse)

            Ok(views.html.viewHelpRequest(loggedInUser, new FrontendHelpRequest(helpRequests.head), isSubscribedToResponses, frontendHelpResponses))
          }

        case None => Redirect(routes.Application.login)
      }
  }

  def editHelpRequest(id: Int) = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(loggedInUser) =>
          val helpRequest = HelpRequestDto.get(Some(Map("id" -> id.toString))).head

          if (helpRequest.requesterId.get == loggedInUser.id.get)
            Ok(views.html.editHelpRequest(loggedInUser, new FrontendHelpRequest(helpRequest)))
          else
            Forbidden("Only your own help requests, you may edit.")
        case None => Redirect(routes.Application.login)
      }
  }

  def msgInbox = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(loggedInUser) =>
          val from = if (request.queryString.contains("from"))
            Some(request.queryString.get("from").get.head)
          else
            None

          Ok(views.html.msgInbox(loggedInUser, from))
        case None => Redirect(routes.Application.login)
      }
  }

  def createMessage = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(loggedInUser) => Ok(views.html.createMessage(loggedInUser))
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