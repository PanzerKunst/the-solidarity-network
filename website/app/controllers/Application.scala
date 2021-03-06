package controllers

import play.api.mvc._
import services.I18nService
import scala.collection.mutable
import models.frontend._
import models.User
import db._
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

      val to = session.get("to")

      val username = if (request.queryString.contains("username"))
        Some(request.queryString.get("username").get.head)
      else
        None

      Ok(views.html.login(username, from, to)).withSession(session - "to")
  }

  def logout = Action {
    Redirect(routes.Application.index).withNewSession
  }

  def home = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(loggedInUser) => Ok(views.html.home(new FrontendUser(loggedInUser), unreadMessagesCount(session)))
        case None => Redirect(routes.Application.login)
      }
  }

  def myProfile = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(loggedInUser) =>
          val references = ReferenceDto.get(Some(Map("to_user_id" -> loggedInUser.id.get.toString)))
          val frontendReferences = for (ref <- references) yield new FrontendReference(ref, loggedInUser)
          Ok(views.html.profile(new FrontendUser(loggedInUser), unreadMessagesCount(session), new FrontendUser(loggedInUser), frontendReferences))
        case None => Redirect(routes.Application.login)
      }
  }

  def editProfile = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(loggedInUser) => Ok(views.html.editProfile(new FrontendUser(loggedInUser), unreadMessagesCount(session), CountryDto.get(None)))
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
          Ok(views.html.profile(new FrontendUser(loggedInUser), unreadMessagesCount(session), new FrontendUser(user), frontendReferences))
        case None => Redirect(routes.Application.login)
      }
  }

  def createHelpRequest = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(loggedInUser) => Ok(views.html.createHelpRequest(new FrontendUser(loggedInUser), unreadMessagesCount(session)))
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

          Ok(views.html.searchHelpRequests(new FrontendUser(loggedInUser), unreadMessagesCount(session), query))
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

          Ok(views.html.helpDashboard(new FrontendUser(loggedInUser), unreadMessagesCount(session), from))
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
            val isSubscribedToReplies = !SubscriptionToHelpRepliesDto.get(Some(filtersMap)).isEmpty

            val frontendHelpReplies = for (helpReply <- HelpReplyDto.get(Some(Map("request_id" -> id.toString)))) yield new FrontendHelpReply(helpReply)

            Ok(views.html.viewHelpRequest(new FrontendUser(loggedInUser), unreadMessagesCount(session), new FrontendHelpRequest(helpRequests.head), isSubscribedToReplies, frontendHelpReplies))
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
            Ok(views.html.editHelpRequest(new FrontendUser(loggedInUser), unreadMessagesCount(session), new FrontendHelpRequest(helpRequest)))
          else
            Forbidden("Only your own help requests, you may edit.")
        case None => Redirect(routes.Application.login)
      }
  }

  def msgInbox = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(loggedInUser) =>
          val inboxMessages = MessageDto.get(Map("to_user_id" -> loggedInUser.id.get.toString))
          val inboxFrontendMessages = for (msg <- inboxMessages) yield new FrontendMessage(msg)

          Ok(views.html.msgInbox(new FrontendUser(loggedInUser), unreadMessagesCount(session), inboxFrontendMessages))
        case None =>
          Redirect(routes.Application.login).withSession(
            session + ("to" -> "messages")
          )
      }
  }

  def createMessage = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(loggedInUser) =>
          val recipient = if (request.queryString.contains("recipient"))
            Some(request.queryString.get("recipient").get.head)
          else
            None

          Ok(views.html.createMessage(new FrontendUser(loggedInUser), unreadMessagesCount(session), recipient))
        case None => Redirect(routes.Application.login)
      }
  }

  def viewMessage(id: Int) = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(loggedInUser) =>
          val messages = MessageDto.get(Map("id" -> id.toString))
          if (messages.length > 0) {
            val msg = messages.head

            val originalMessage = msg.replyToMessageId match {
              case Some(parentMessageId) => MessageDto.get(Map("id" -> parentMessageId.toString)).head
              case None => msg
            }

            val replies = MessageDto.get(Map("reply_to_message_id" -> originalMessage.id.get.toString), false)
            val frontendReplies = for (reply <- replies) yield new FrontendMessage(reply)

            MessageDto.markAsRead(msg)
            recalculateUnreadMessagesCount(session)

            Ok(views.html.viewMessage(new FrontendUser(loggedInUser), unreadMessagesCount(session), new FrontendMessage(originalMessage), frontendReplies))
          } else {
            NotFound
          }
        case None => Redirect(routes.Application.login)
      }
  }

  def sentMessages = Action {
    implicit request =>
      loggedInUser(session) match {
        case Some(loggedInUser) =>
          val sentMessages = MessageDto.get(Map("from_user_id" -> loggedInUser.id.get.toString))
          val sentFrontendMessages = for (msg <- sentMessages) yield new FrontendMessage(msg)

          Ok(views.html.sentMessages(new FrontendUser(loggedInUser), unreadMessagesCount(session), sentFrontendMessages))
        case None => Redirect(routes.Application.login)
      }
  }

  def loggedInUser(session: Session): Option[User] = {
    session.get("userId") match {
      case Some(userId) =>
        val matchingUsers = UserDto.get(Some(Map("id" -> userId)))

        if (matchingUsers.isEmpty)
          None
        else
          Some(matchingUsers.head)
      case None => None
    }
  }

  private def unreadMessagesCount(session: Session): Int = {
    session.get("unreadMessagesCount") match {
      case Some(unreadMessagesCount) =>
        unreadMessagesCount.asInstanceOf[Int]
      case None =>
        // Get value and store it in session
        val filtersMap = Map("to_user_id" -> loggedInUser(session).get.id.get.toString,
          "is_read" -> "0")

        val unreadMessagesCount = MessageDto.get(filtersMap).length

        session + ("unreadMessagesCount" -> unreadMessagesCount.toString)

        unreadMessagesCount
    }
  }

  private def recalculateUnreadMessagesCount(session: Session) {
    session - ("unreadMessagesCount")
  }
}
