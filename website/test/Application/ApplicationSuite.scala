package Application

import play.test.UnitFunSuite
import controllers.Application
import database.webapp.CustomizedWebappRepository
import models.webapp.CustomizedWebapp

class ApplicationSuite extends UnitFunSuite {

  test("generateUniqueWebappName") {
    CustomizedWebappRepository.create(new CustomizedWebapp("My Duty Plan", "", "",0))
    CustomizedWebappRepository.create(new CustomizedWebapp("My Traffic Info", "", "",1))
    CustomizedWebappRepository.create(new CustomizedWebapp("My Traffic Info 2", "", "",2))

    assert(Application.generateUniqueWebappNameFromTemplate("Foo") === "My Foo")
    assert(Application.generateUniqueWebappNameFromTemplate("Duty Plan") === "My Duty Plan 2")
    assert(Application.generateUniqueWebappNameFromTemplate("Traffic Info") === "My Traffic Info 3")
  }

}
