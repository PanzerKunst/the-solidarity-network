package test

import play.data.validation.Validation
import play.test.UnitFunSuite
import models.{SignupForPreview, SignupForPreviewFormInfo}
import database.SignupForPreviewRepository
import io.{BufferedSource, Source}
import common.JsonUtil


class SignupForPreviewSuite extends UnitFunSuite {

  test("Correct SignupForPreviewFormInfo object from JSON") {
    val validationResult = Validation.valid("signupForPreviewFormInfo", getSampleSignupForPreviewFormInfo(getFullPath("DefinedDevices.json")))
    assert(validationResult.ok)
  }

  test("No name SignupForPreviewFormInfo object from JSON") {
    val validationResult = Validation.valid("signupForPreviewFormInfo", getSampleSignupForPreviewFormInfo(getFullPath("NoName.json")))
    assert(!validationResult.ok)
  }

  test("No email SignupForPreviewFormInfo object from JSON") {
    val validationResult = Validation.valid("signupForPreviewFormInfo", getSampleSignupForPreviewFormInfo(getFullPath("NoEmail.json")))
    assert(!validationResult.ok)
  }

  test("Incorrect email SignupForPreviewFormInfo object from JSON") {
    val validationResult = Validation.valid("signupForPreviewFormInfo", getSampleSignupForPreviewFormInfo(getFullPath("IncorrectEmail.json")))
    assert(!validationResult.ok)
  }

  test("No defined device into couchDB") {
    SignupForPreviewRepository.create(getSampleSignupForPreview(getFullPath("NoDefinedDevices.json")))
  }

  test("Some defined devices into couchDB") {
    SignupForPreviewRepository.create(getSampleSignupForPreview(getFullPath("DefinedDevices.json")))
  }

  private def getFullPath(fileName: String): String = {
    "test/SignupForPreview/" + fileName
  }

  private def getSampleSignupForPreviewFormInfo(fileName: String): SignupForPreviewFormInfo = {
    var fileSource: BufferedSource = null
    try {
      fileSource = Source.fromFile(fileName)
      JsonUtil.parse(fileSource.mkString, classOf[SignupForPreviewFormInfo])
    }
    finally fileSource match {
      case _ => fileSource.close
    }
  }

  private def getSampleSignupForPreview(fileName: String): SignupForPreview = {
    val signupForPreviewFormInfo: SignupForPreviewFormInfo = getSampleSignupForPreviewFormInfo(fileName)

    if (Validation.valid("signupForPreviewFormInfo", signupForPreviewFormInfo).ok)
      new SignupForPreview(signupForPreviewFormInfo)
    else
      null
  }
}
