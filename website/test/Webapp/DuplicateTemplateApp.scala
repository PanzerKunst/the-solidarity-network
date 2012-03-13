package Webapp

import play.test.UnitFunSuite
import play.data.validation.Validation
import models.frontend.FrontendWebapp
import database.UserRepository
import models.webapp.CustomizedWebapp
import database.webapp.CustomizedWebappRepository
import io.{BufferedSource, Source}
import common.JsonUtil
import models.User


class DuplicateTemplateApp extends UnitFunSuite {

  test("Duplication of a template app and successful save (creation)") {
    createUser("appeartester")

    val frontendWebapp = createSampleFrontendWebappFromTemplate("FrontendWebappToCreate.json", "duty-plan")

    var validationResult = Validation.valid("frontendWebapp", frontendWebapp)
    assert(validationResult.ok, "Invalid frontendWebapp")

    // We delete it in case it already exists
    CustomizedWebappRepository.delete(new CustomizedWebapp(frontendWebapp))

    UserRepository.getUserOfUsername(frontendWebapp.username) match {
      case Some(user) => {
        // Creation
        val createdCustomizedWebapp = CouchDbWebappApi.createWebappFromFrontend(frontendWebapp, user)
        validationResult = Validation.valid("createdCustomizedWebapp", createdCustomizedWebapp)
        assert(validationResult.ok, "Invalid createdCustomizedWebapp")

        // Verification that the customized webapp is in the database
        val customizedWebappFromDb = CustomizedWebappRepository.get(createdCustomizedWebapp.getId)
        validationResult = Validation.valid("customizedWebappFromDb", customizedWebappFromDb)
        assert(validationResult.ok, "Invalid customizedWebappFromDb")

        // Finally, we delete the created customized webapp
        CustomizedWebappRepository.delete(customizedWebappFromDb)
      }
      case None => fail("No user")
    }
  }

  private def createUser(username: String) {
    UserRepository.add(new User(username, username))
  }

  private def createSampleFrontendWebappFromTemplate(fileName: String, templateWebappId: String): FrontendWebapp = {
    val frontendWebapp = getSampleFrontendWebapp(getFullPath(fileName))
    frontendWebapp.templateWebappId = templateWebappId
    frontendWebapp
  }

  private def getFullPath(fileName: String): String = {
    "test/Webapp/" + fileName
  }

  private def getSampleFrontendWebapp(filePath: String): FrontendWebapp = {
    var fileSource: BufferedSource = null
    try {
      fileSource = Source.fromFile(filePath)
      JsonUtil.parse(fileSource.mkString, classOf[FrontendWebapp])
    }
    finally fileSource match {
      case _ => fileSource.close
    }
  }
}