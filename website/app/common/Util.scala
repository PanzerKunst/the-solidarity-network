package common

import play.Play

object Util {
  def getSystemPropertyOrPlayConfig(key: String) = {
    val systemProperty = System.getProperty(key)
    if (systemProperty == null)
      Play.configuration.get(key).toString
    else
      systemProperty
  }
}
