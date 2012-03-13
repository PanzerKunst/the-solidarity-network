package Webapp

import play.test.FunctionalTest._
import database.webapp.{UserTemplateWebappRepository, CustomizedWebappRepository}
import common.{JsonUtil, FunctionalTestBase}
import models.frontend.FrontendWebapp
import models.{User, Launchable}
import java.io.ByteArrayInputStream

class WebappApiSpec extends FunctionalTestBase {
  val TestUser = new User("test", "Tester")

  feature("Webapp API") {
    info("As Javascript, I want to make AJAX requests to manage webapps")


    /**
     * Creating a Customized Webapp
     */

    scenario("Creating a Customized Webapp") {
      given("A logged in user")
      login(TestUser)

      val id = "myId"
      val name = "My App"

      val docType = "incident"

      val incidentNumber1 = 123456L
      val incidentTitle1 = "1st Incident Title"

      val incidentNumber2 = 234567L
      val incidentTitle2 = "2nd Incident Title"

      val incidentNumber3 = 345678L
      val incidentTitle3 = "3rd Incident Title"


      when("Creating a customized webapp")
      val response = POST("/db/webapps2", "application/json",
        """{
              "webApp": {
                "name":"""" + name + """",
                "description":"some desc",
                "type":"customizedWebapp",
                "templateWebappId":"traffic-info",
                "id":"""" + id + """"
              },
              "data": {
                "create": [
                  {
                      "no":""" + incidentNumber1 + """,
                      "title":"""" + incidentTitle1 + """",
                      "summary":"This is incident was created at the same time than the app",
                      "assetNo":123,
                      "assetType":"Router",
                      "assigneeId":31,
                      "assigneeName": "JOHN RAMBO",
                      "status":1,
                      "priority":1,
                      "urgency":1,
                      "impact":1,
                      "createdDate":"2012-03-11 15:00",
                      "reportedByName":"DENISE PITHION",
                      "docType": """" + docType + """",
                      "published": false
                  },
                  {
                      "no":""" + incidentNumber2 + """,
                      "title":"""" + incidentTitle2 + """",
                      "summary":"This is incident was created at the same time than the app",
                      "assetNo":234,
                      "assetType":"Router",
                      "assigneeId":32,
                      "assigneeName": "JOHN RAMBO",
                      "status":1,
                      "priority":1,
                      "urgency":1,
                      "impact":1,
                      "createdDate":"2012-03-11 15:00",
                      "reportedByName":"DENISE PITHION",
                      "docType": """" + docType + """",
                      "published": false
                  },
                  {
                    "no":""" + incidentNumber3 + """,
                    "title":"""" + incidentTitle3 + """",
                    "summary":"This is incident was created at the same time than the app",
                    "assetNo":345,
                    "assetType":"Router",
                    "assigneeId":33,
                    "assigneeName": "JOHN RAMBO",
                    "status":1,
                    "priority":1,
                    "urgency":1,
                    "impact":1,
                    "createdDate":"2012-03-11 15:00",
                    "reportedByName":"DENISE PITHION",
                    "docType": """" + docType + """",
                    "published": false
                  }
                ]
              }
           }""")

      then("I should receive an OK response")
      assertStatus(200, response)
//      val in = new ByteArrayInputStream(response.out.toByteArray)
//      val string = IOUtils.toString(in, "UTF-8")
      var jsonObject = JsonUtil.parse(new ByteArrayInputStream(response.out.toByteArray)).getAsJsonObject

      and("response should contain element webApp")
      val webappNode = jsonObject.get("webApp")
      assert(webappNode!=null)

      and("response should contain element data")
      val dataNode = jsonObject.get("data")
      assert(dataNode != null)
      and("data should contain element create")
      val createNode = dataNode.getAsJsonObject.get("create")
      val createdDocuments =createNode.getAsJsonArray
      and("create should contain 3 elements")
      assert(createdDocuments.size()==3)
      
      and("webapp should be created in the database")
      val webapp = CustomizedWebappRepository.get(id)
      assert((webapp != null) === true)
      assert(webapp.getName() === name)

      logout()
    }


    /**
     * Reordering two published userTemplateWebapps
     */

    scenario("Reordering two published userTemplateWebapps") {
      given("A logged in user")
      login(TestUser)


      when("Creating userTemplateWebapp 1")

      val id1 = "userTemplateWebapp1"
      val name1 = "Duty Plan"
      val templateWebappId1 = "duty-plan"
      val sortOrder1 = 0

      POST("/db/webapps", "application/json",
        """{"name":"""" + name1 + """",
        "description":"some desc",
        "type":"userTemplateWebapp",
        "templateWebappId":"""" + templateWebappId1 + """",
        "id":"""" + id1 + """",
        "sortOrder":""" + sortOrder1 + """,
        "status":"""" + Launchable.statusToBePublished + """"}""")

      then("It should be created in the database")
      var webapp1 = UserTemplateWebappRepository.get(id1)
      assert((webapp1 != null) === true)
      assert(webapp1.getName() === name1)


      when("Creating userTemplateWebapp 2")

      val id2 = "userTemplateWebapp2"
      val name2 = "Traffic Info"
      val templateWebappId2 = "traffic-info"
      val sortOrder2 = 1

      POST("/db/webapps", "application/json",
        """{"name":"""" + name2 + """",
        "description":"some desc",
        "type":"userTemplateWebapp",
        "templateWebappId":"""" + templateWebappId2 + """",
        "id":"""" + id2 + """",
        "sortOrder":""" + sortOrder2 + """,
        "status":"""" + Launchable.statusToBePublished + """"}""")


      then("It should be created in the database")
      var webapp2 = UserTemplateWebappRepository.get(id2)
      assert((webapp2 != null) === true)
      assert(webapp2.getName() === name2)


      when("Publishing webapps")

      POST("/db/launchables/publish", "application/json", "")

      then("Status for the 2 userTemplateWebapps in the database is 'published'")
      webapp1 = UserTemplateWebappRepository.get(id1)
      assert(webapp1.getStatus() === Launchable.statusPublished)

      webapp2 = UserTemplateWebappRepository.get(id2)
      assert(webapp2.getStatus() === Launchable.statusPublished)


      when("Changing status and sortOrder on both in the database")

      var frontendWebapp1 = new FrontendWebapp(webapp1, TestUser)
      frontendWebapp1.setStatus(Launchable.statusToReorderPublished)
      frontendWebapp1.setSortOrder(1)

      PUT("/db/webapps/" + webapp1.getId,
        "application/json",
        JsonUtil.serialize(frontendWebapp1))

      var frontendWebapp2 = new FrontendWebapp(webapp2, TestUser)
      frontendWebapp2.setStatus(Launchable.statusToReorderPublished)
      frontendWebapp2.setSortOrder(0)

      PUT("/db/webapps/" + webapp2.getId,
        "application/json",
        JsonUtil.serialize(frontendWebapp2))


      then("Check in the database that status and sortOrder is updated")

      webapp1 = UserTemplateWebappRepository.get(id1)
      assert(webapp1.getStatus() === Launchable.statusToReorderPublished)
      assert(webapp1.getSortOrder() === 1)

      webapp2 = UserTemplateWebappRepository.get(id2)
      assert(webapp2.getStatus() === Launchable.statusToReorderPublished)
      assert(webapp2.getSortOrder() === 0)


      when("Asking for publication")

      POST("/db/launchables/publish", "application/json", "")

      then("Status for the 2 userTemplateWebapps in the database is 'published'")
      webapp1 = UserTemplateWebappRepository.get(id1)
      assert(webapp1.getStatus() === Launchable.statusPublished)

      webapp2 = UserTemplateWebappRepository.get(id2)
      assert(webapp2.getStatus() === Launchable.statusPublished)


      when("Changing the status to toBeUnPublished")

      frontendWebapp1 = new FrontendWebapp(webapp1, TestUser)
      frontendWebapp1.setStatus(Launchable.statusToBeUnpublished)

      PUT("/db/webapps/" + webapp1.getId,
        "application/json",
        JsonUtil.serialize(frontendWebapp1))

      frontendWebapp2 = new FrontendWebapp(webapp2, TestUser)
      frontendWebapp2.setStatus(Launchable.statusToBeUnpublished)

      PUT("/db/webapps/" + webapp2.getId,
        "application/json",
        JsonUtil.serialize(frontendWebapp2))


      then("Check in the database that status is updated")

      webapp1 = UserTemplateWebappRepository.get(id1)
      assert(webapp1.getStatus() === Launchable.statusToBeUnpublished)

      webapp2 = UserTemplateWebappRepository.get(id2)
      assert(webapp2.getStatus() === Launchable.statusToBeUnpublished)


      when("Asking for publication")

      POST("/db/launchables/publish", "application/json", "")


      then("The userTemplateWebapps shouldn't exist in the database anymore")

      webapp1 = UserTemplateWebappRepository.get(id1)
      assert(webapp1 == null)

      webapp2 = UserTemplateWebappRepository.get(id2)
      assert(webapp2 == null)


      logout()
    }


    /**
     * Publishing a customizedWebapp and checking the sortOrder is correct
     */
    scenario("Publishing a customizedWebapp and checking the sortOrder is correct") {
      given("A logged in user")
      login(TestUser)


      when("Creating a userTemplateWebapp")

      val id1 = "userTemplateWebapp1"
      val name1 = "Duty Plan"
      val templateWebappId1 = "duty-plan"
      val sortOrder1 = 0

      POST("/db/webapps", "application/json",
        """{"name":"""" + name1 + """",
        "description":"some desc",
        "type":"userTemplateWebapp",
        "templateWebappId":"""" + templateWebappId1 + """",
        "id":"""" + id1 + """",
        "sortOrder":""" + sortOrder1 + """,
        "status":"""" + Launchable.statusToBePublished + """"}""")

      then("It should be created in the database")

      var userTemplateWebapp = UserTemplateWebappRepository.get(id1)
      assert((userTemplateWebapp != null) === true)
      assert(userTemplateWebapp.getName() === name1)


      when("Creating customizedWebapp")

      val id2 = "customizedWebapp1"
      val name2 = "CBR's Traffic Info"
      val templateWebappId2 = "traffic-info"

      POST("/db/webapps", "application/json",
        """{"name":"""" + name2 + """",
        "description":"some desc",
        "type":"customizedWebapp",
        "templateWebappId":"""" + templateWebappId2 + """",
        "id":"""" + id2 + """"}""")

      then("It should be created in the database")

      var customizedWebapp = CustomizedWebappRepository.get(id2)
      assert((customizedWebapp != null) === true)
      assert(customizedWebapp.getName() === name2)
      assert(customizedWebapp.getStatus() === Launchable.statusSaved)


      when("Updating the status to 'toBePublished' and sortOrder to '1'")

      var frontendCustomizedWebapp = new FrontendWebapp(customizedWebapp, TestUser)
      frontendCustomizedWebapp.setStatus(Launchable.statusToBePublished)
      frontendCustomizedWebapp.setSortOrder(1)

      PUT("/db/webapps/" + customizedWebapp.getId,
        "application/json",
        JsonUtil.serialize(frontendCustomizedWebapp))

      then("Check in the database that webapp is updated")

      customizedWebapp = CustomizedWebappRepository.get(id2)
      assert(customizedWebapp.getStatus() === Launchable.statusToBePublished)
      assert(customizedWebapp.getSortOrder() === 1)


      when("Asking for publication")

      POST("/db/launchables/publish", "application/json", "")

      then("Status for the 2 webapps in the database is 'published', and the sortOrder is correct")
      userTemplateWebapp = UserTemplateWebappRepository.get(id1)
      assert(userTemplateWebapp.getStatus() === Launchable.statusPublished)
      assert(userTemplateWebapp.getSortOrder() === 0)

      customizedWebapp = CustomizedWebappRepository.get(id2)
      assert(customizedWebapp.getStatus() === Launchable.statusPublished)
      assert(customizedWebapp.getSortOrder() === 1)


      when("Changing the status to toBeUnPublished")

      var frontendUserTemplateWebapp = new FrontendWebapp(userTemplateWebapp, TestUser)
      frontendUserTemplateWebapp.setStatus(Launchable.statusToBeUnpublished)

      PUT("/db/webapps/" + frontendUserTemplateWebapp.getId,
        "application/json",
        JsonUtil.serialize(frontendUserTemplateWebapp))

      frontendCustomizedWebapp = new FrontendWebapp(customizedWebapp, TestUser)
      frontendCustomizedWebapp.setStatus(Launchable.statusToBeUnpublished)

      PUT("/db/webapps/" + frontendCustomizedWebapp.getId,
        "application/json",
        JsonUtil.serialize(frontendCustomizedWebapp))


      then("Check in the database that status is updated")

      userTemplateWebapp = UserTemplateWebappRepository.get(id1)
      assert(userTemplateWebapp.getStatus() === Launchable.statusToBeUnpublished)

      customizedWebapp = CustomizedWebappRepository.get(id2)
      assert(customizedWebapp.getStatus() === Launchable.statusToBeUnpublished)


      when("Asking for publication")

      POST("/db/launchables/publish", "application/json", "")


      then("The userTemplateWebapps shouldn't exist in the database anymore")

      userTemplateWebapp = UserTemplateWebappRepository.get(id1)
      assert(userTemplateWebapp == null)

      customizedWebapp = CustomizedWebappRepository.get(id2)
      assert(customizedWebapp.getStatus() === Launchable.statusSaved)


      logout()
    }
  }

}
