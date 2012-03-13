package common

import org.apache.http.HttpStatus
import org.scalatest.{GivenWhenThen, FeatureSpec}
import play.test.FunctionalTest
import play.mvc.Http.Response
import play.mvc.Http
import models.User

abstract class FunctionalTestBase extends FunctionalTest with FeatureSpec with GivenWhenThen {
  def login(user: User) {
    val response = FunctionalTest.POST("/doLogin", FunctionalTest.APPLICATION_X_WWW_FORM_URLENCODED,
      "loginUser.username=" + user.username + "&loginUser.password=" + user.username)
    assert(response.status === HttpStatus.SC_MOVED_TEMPORARILY)
  }

  def logout() {
    val response = FunctionalTest.GET("/logout")
    assert(response.status === HttpStatus.SC_MOVED_TEMPORARILY)
  }

  // Workaround for a bug in Play Framework 1.2.4
  def PUT(url: AnyRef, contenttype: String, body: String): Response = {
    val request = FunctionalTest.newRequest()

    // Using reflection to access private field
    val cls = Class.forName("play.test.FunctionalTest")
    val field = cls.getDeclaredField("savedCookies")
    field.setAccessible(true)
    val savedCookies = field.get(null)

    if (savedCookies != null) request.cookies = savedCookies.asInstanceOf[java.util.Map[String, Http.Cookie]]
    FunctionalTest.PUT(request, url, contenttype, body)
  }
}
