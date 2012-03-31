package common

import java.io.InputStream
import org.codehaus.jackson.JsonNode
import org.codehaus.jackson.map.ObjectMapper
import org.codehaus.jackson.node.ObjectNode
import com.codahale.jerkson.ScalaModule

object JsonUtil {
  def parse[T](inputStream: InputStream, classOfT: Class[T]) = {
    createObjectMapper().readValue(inputStream, classOfT)
  }

  def parse[T](json: String, classOfT: Class[T]) = {
    createObjectMapper().readValue(json, classOfT)
  }

  def serialize(obj: AnyRef): String = {
    createObjectMapper().writeValueAsString(obj)
  }

  def parse(inputStream: InputStream): JsonNode = {
    createObjectMapper().readTree(inputStream)
  }

  def parse(json: String): JsonNode = {
    createObjectMapper().readTree(json)
  }

  def copyObjectNode(obj: ObjectNode): ObjectNode = {
    createObjectMapper().readTree(obj.traverse()).asInstanceOf[ObjectNode]
  }

  private def createObjectMapper() = {
    val mapper = new ObjectMapper()
    mapper.registerModule(new ScalaModule(this.getClass.getClassLoader))
    mapper
  }
}
