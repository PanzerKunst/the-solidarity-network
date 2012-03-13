package controllers

import play.mvc.Controller
import views.Application._
import models.User

object Application extends Controller {

  def home() = {
    html.home()
  }

  def loggedInUser(): Option[User] = {
    val username = session.get("username")
    val name = session.get("name")
    if (username == null || name == null)
      None
    else
      Some(new User(username, name))
  }

  def isLoggedIn: Boolean = {
    session.get("username") != null
  }

  private def doNotCachePage {
    response.setHeader("Cache-Control", "no-store")
    response.setHeader("Pragma", "no-cache")
    response.setHeader("Expires", "0")
  }
}
