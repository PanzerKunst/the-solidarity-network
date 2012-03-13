package Enquire

import play.data.validation.Validation
import play.test.UnitFunSuite
import models.{EnquiryFormInfo, Enquiry}
import database.EnquiryRepository
import io.{BufferedSource, Source}
import common.JsonUtil


class EnquirySuite extends UnitFunSuite {

  test("Correct Enquiry object from JSON") {
    val validationResult = Validation.valid("enquiry", getSampleEnquiryFormInfo(getFullPath("AMessage.json")))
    assert(validationResult.ok)
  }

  test("No name Enquiry object from JSON") {
    val validationResult = Validation.valid("enquiryFormInfo", getSampleEnquiryFormInfo(getFullPath("NoName.json")))
    assert(!validationResult.ok)
  }

  test("No email Enquiry object from JSON") {
    val validationResult = Validation.valid("enquiry", getSampleEnquiryFormInfo(getFullPath("NoEmail.json")))
    assert(!validationResult.ok)
  }

  test("Incorrect email Enquiry object from JSON") {
    val validationResult = Validation.valid("enquiry", getSampleEnquiryFormInfo(getFullPath("IncorrectEmail.json")))
    assert(!validationResult.ok)
  }

  test("No message into couchDB") {
    EnquiryRepository.create(getSampleEnquiry(getFullPath("NoMessage.json")))
  }

  test("Some message into couchDB") {
    EnquiryRepository.create(getSampleEnquiry(getFullPath("AMessage.json")))
  }

  private def getFullPath(fileName: String): String = {
    "test/Enquiry/" + fileName
  }

  private def getSampleEnquiryFormInfo(fileName: String): EnquiryFormInfo = {
    var fileSource: BufferedSource = null
    try {
      fileSource = Source.fromFile(fileName)
      JsonUtil.parse(fileSource.mkString, classOf[EnquiryFormInfo])
    }
    finally fileSource match {
      case _ => fileSource.close
    }
  }

  private def getSampleEnquiry(fileName: String): Enquiry = {
    val enquiryFormInfo: EnquiryFormInfo = getSampleEnquiryFormInfo(fileName)

    if (Validation.valid("enquiryFormInfo", enquiryFormInfo).ok)
      new Enquiry(enquiryFormInfo)
    else
      null
  }
}
