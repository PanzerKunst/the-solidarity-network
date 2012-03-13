package BusinessDocument

import play.test.FunctionalTest._
import models.User
import common.{JsonUtil, FunctionalTestBase}
import scala.collection.JavaConversions._
import collection.mutable.HashMap
import com.google.gson.JsonObject
import java.io._

class BusinessDocumentSpec extends FunctionalTestBase {

  val TestUser = new User("test", "Tester")
  val WebappId = "D2B72EC1-1E74-4117-B2D0-BE981C8164AA"
  val docType = "incident"


  feature("Business Document API") {
    info("As Javascript, I want to make AJAX requests to manage business documents")

    scenario("Retrieving all business document for a Webapp") {
      given("A logged in user")
      login(TestUser)

      val incidentNumber1 = 12345L
      val incidentTitle1 = "1st Incident Title"

      when("Adding 1st business document")
      var response = POST("/db/businessdocuments/" + WebappId, "application/json",
        """{
            "no":""" + incidentNumber1 + """,
            "title":"""" + incidentTitle1 + """",
            "summary":"This is a summary of the incident",
            "assetNo":123,
            "assetType":"Router",
            "assigneeId":33,
            "assigneeName": "JOHN DOE",
            "status":1,
            "priority":1,
            "urgency":1,
            "impact":1,
            "createdDate":"2010-12-24 15:00",
            "reportedByName":"DENISE PITHION",
            "docType": """" + docType + """",
            "published": false
        } """
      )

      then("I should receive an OK response")
      assertStatus(200, response)
      and("Return document should have an id")
      var jsonObject = JsonUtil.parse(new ByteArrayInputStream(response.out.toByteArray)).getAsJsonObject
      assert(incidentNumber1 == jsonObject.get("no").getAsLong)
      assert(incidentTitle1 == jsonObject.get("title").getAsString)
      assert(docType == jsonObject.get("docType").getAsString)
      val incidentId1 = jsonObject.get("_id").getAsString


      val incidentNumber2 = 23456L
      val incidentTitle2 = "2nd Incident Title"

      when("Adding 2nd business document")
      response = POST("/db/businessdocuments/" + WebappId, "application/json",
        """{
            "no":""" + incidentNumber2 + """,
            "title":"""" + incidentTitle2 + """",
            "summary":"This is a summary of the incident",
            "assetNo":123,
            "assetType":"Router",
            "assigneeId":33,
            "assigneeName": "JOHN DOE",
            "status":1,
            "priority":1,
            "urgency":1,
            "impact":1,
            "createdDate":"2010-12-24 15:00",
            "reportedByName":"DENISE PITHION",
            "docType": """" + docType + """",
            "published": false
        } """
      )

      then("I should receive an OK response")
      assertStatus(200, response)
      and("Return document should have an id")
      jsonObject = JsonUtil.parse(new ByteArrayInputStream(response.out.toByteArray)).getAsJsonObject
      assert(incidentNumber2 == jsonObject.get("no").getAsLong)
      assert(incidentTitle2 == jsonObject.get("title").getAsString)
      assert(docType == jsonObject.get("docType").getAsString)
      val incidentId2 = jsonObject.get("_id").getAsString


      val incidentNumber3 = 34567L
      val incidentTitle3 = "3rd Incident Title"

      when("Adding 3rd business document")
      response = POST("/db/businessdocuments/" + WebappId, "application/json",
        """{
            "no":""" + incidentNumber3 + """,
            "title":"""" + incidentTitle3 + """",
            "summary":"This is a summary of the incident",
            "assetNo":123,
            "assetType":"Router",
            "assigneeId":33,
            "assigneeName": "JOHN DOE",
            "status":1,
            "priority":1,
            "urgency":1,
            "impact":1,
            "createdDate":"2010-12-24 15:00",
            "reportedByName":"DENISE PITHION",
            "docType": """" + docType + """",
            "published": false
        } """
      )

      then("I should receive an OK response")
      assertStatus(200, response)
      and("Return document should have an id")
      jsonObject = JsonUtil.parse(new ByteArrayInputStream(response.out.toByteArray)).getAsJsonObject
      assert(incidentNumber3 == jsonObject.get("no").getAsLong)
      assert(incidentTitle3 == jsonObject.get("title").getAsString)
      assert(docType == jsonObject.get("docType").getAsString)
      val incidentId3 = jsonObject.get("_id").getAsString


      when("Retrieving all business document for a Webapp")
      response = GET("/db/businessdocuments/" + WebappId)
      then("I should receive an OK response")
      assertStatus(200, response)
      and("Response should contain 3 business documents")
      //val responseBody: String = convertStreamToString(new ByteArrayInputStream(response.out.toByteArray))
      var jsonArray = JsonUtil.parse(new ByteArrayInputStream(response.out.toByteArray)).getAsJsonArray
      assert(jsonArray.size()==3)
      and("With same docType")
      val map = HashMap[String, JsonObject ]()
      for(jsonElement <- jsonArray.iterator){
        jsonObject = jsonElement.getAsJsonObject
        assert(docType == jsonObject.get("docType").getAsString)
        map.put(jsonObject.get("_id").getAsString, jsonObject)
      }
      assert(map.contains(incidentId1))
      assert(map.contains(incidentId2))
      assert(map.contains(incidentId3))

      when("Deleting 2nd business document")
      response = DELETE("/db/businessdocuments/" + WebappId + "/"+ incidentId2)
      then("I should receive an OK response")
      assertStatus(200, response)

      when("Retrieving all business document for a Webapp")
      response = GET("/db/businessdocuments/" + WebappId)
      then("I should receive an OK response")
      assertStatus(200, response)
      and("Response should contain 3 business documents")
      //val responseBody: String = convertStreamToString(new ByteArrayInputStream(response.out.toByteArray))
      jsonArray = JsonUtil.parse(new ByteArrayInputStream(response.out.toByteArray)).getAsJsonArray
      assert(jsonArray.size()==2)

      logout()
    }
  }

  //TODO: clean up since not used
  def convertStreamToString(is : InputStream) : String = {
    def inner(reader : BufferedReader, sb : StringBuilder) : String = {
      val line = reader.readLine()
      if(line != null) {
        try {
          inner(reader, sb.append(line + "\n"))
        } catch {
          case e : IOException => e.printStackTrace()
        } finally {
          try {
            is.close()
          } catch {
            case e : IOException => e.printStackTrace()
          }
        }

      }
      sb.toString()
    }

    inner(new BufferedReader(new InputStreamReader(is)), new StringBuilder())
  }
}
