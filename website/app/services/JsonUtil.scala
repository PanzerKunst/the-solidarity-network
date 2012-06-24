package services

import org.codehaus.jackson.map.ObjectMapper
import com.codahale.jerkson.ScalaModule
import play.api.libs.json.{Json, JsValue}
import play.api.Logger
import play.api.libs.ws.Response


object JsonUtil {
  def parse[T](json: String, classOfT: Class[T]) = {
    try {
      createObjectMapper().readValue(json, classOfT)
    } catch {
      case e: Throwable => {
        Logger.error("Unable to parse JSON into " + classOfT.getName + ": " + json, e);
        throw e
      }
    }
  }

  def parseResponse[T](response: Response, classOfT: Class[T]) = {
    if (response.header("Content-Type").isDefined && response.header("Content-Type").get.startsWith("application/json")) {
      try {
        createObjectMapper().readValue(response.getAHCResponse.getResponseBodyAsStream, classOfT)
      } catch {
        case e: Throwable => {
          Logger.error("Unable to parse JSON into " + classOfT.getName, e);
          throw e
        }
      }
    } else {
      throw new Exception("Expecting JSON, but got " + response.body)
    }
  }

  def parseJsValue[T](json: JsValue, classOfT: Class[T]) = {
    parse(Json.stringify(json), classOfT)
  }

  def serialize(obj: AnyRef): String = {
    createObjectMapper().writeValueAsString(obj)
  }

  private def createObjectMapper() = {
    val mapper = new ObjectMapper()
    mapper.registerModule(new ScalaModule(this.getClass.getClassLoader))
    mapper
  }
}
