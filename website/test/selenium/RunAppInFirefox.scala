package selenium

import org.openqa.selenium.firefox.{FirefoxProfile, FirefoxDriver}
import play.api.test.{TestServer, FakeApplication, TestBrowser}

trait RunAppInFirefox {
  /* def fakeServer(block: => TestBrowser => A): A = {

    running(TestServer(port = 3333, application = FakeApplication()) {
      val profile = new FirefoxProfile()
      val browser = TestBrowser(new FirefoxDriver(profile))
      block(browser)
    }
  } */
}
