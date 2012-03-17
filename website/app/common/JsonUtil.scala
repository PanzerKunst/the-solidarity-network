package common

import java.io.{InputStreamReader, InputStream}
import com.google.gson.{JsonParser, JsonElement, JsonSyntaxException, Gson}

object JsonUtil {
  def parse[T](inputStream: InputStream, classOfT: Class[T]) = {
    val obj = new Gson().fromJson(new InputStreamReader(inputStream, "UTF-8"), classOfT)
    if (obj == null) {
      throw new JsonSyntaxException("Invalid JSON")
    }
    obj
  }

  def parse[T](json: String, classOfT: Class[T]) = {
    val obj = new Gson().fromJson(json, classOfT)
    if (obj == null) {
      throw new JsonSyntaxException("Invalid JSON: " + json)
    }
    obj
  }

  def parse[T](json: JsonElement, classOfT: Class[T]) = {
    new Gson().fromJson(json, classOfT)
  }

  def parse(inputStream: InputStream): JsonElement = {
    new JsonParser().parse(new InputStreamReader(inputStream, "UTF-8"))
  }

  def parse(json: String): JsonElement = {
    new JsonParser().parse(json)
  }

  def serialize(obj: AnyRef): String = {
    new Gson().toJson(obj)
  }

  def serialize(obj: JsonElement): String = {
    new Gson().toJson(obj)
  }
}


