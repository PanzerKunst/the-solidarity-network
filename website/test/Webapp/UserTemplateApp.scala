package Webapp

import play.test.UnitFunSuite
import play.data.validation.Validation
import models.frontend.FrontendWebapp
import database.UserRepository
import io.{BufferedSource, Source}
import common.JsonUtil
import database.webapp.{TemplateWebappRepository, UserTemplateWebappRepository}


class UserTemplateApp extends UnitFunSuite {

  test("Creation of a UserTemplateWebbapp and successful save (creation)") {
    val templateWebApp = TemplateWebappRepository.get("duty-plan")
    val frontendWebapp = createSampleFrontendWebappFromTemplate("FrontendWebappOfTypeUserTemplateToCreate.json", "duty-plan")

    var validationResult = Validation.valid("frontendWebapp", frontendWebapp)
    assert(validationResult.ok)

    UserRepository.getUserOfUsername(frontendWebapp.username) match {
      case Some(user) => {
        // We delete it in case it already exists

        UserTemplateWebappRepository.getUserTemplateWebappByIdAndUser(templateWebApp, user) match {
          case Some(userTemplateWebappToDelete) => UserTemplateWebappRepository.delete(userTemplateWebappToDelete)
          case None => None
        }

        // Creation
        val createdUserTemplateWebapp = CouchDbWebappApi.createWebappFromFrontend(frontendWebapp, user)
        validationResult = Validation.valid("createdUserTemplateWebapp", createdUserTemplateWebapp)
        assert(validationResult.ok)

        // Verification that the customized webapp is in the database
        val userTemplateWebappFromDb = UserTemplateWebappRepository.get(createdUserTemplateWebapp.getId)
        validationResult = Validation.valid("userTemplateFromDb", userTemplateWebappFromDb)
        assert(validationResult.ok)

        // Finally, we delete the created customized webapp
        UserTemplateWebappRepository.delete(userTemplateWebappFromDb)
      }
      case None => assert(false)
    }
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