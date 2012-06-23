package services

import collection.mutable
import play.api.i18n.{Messages, Lang}

object I18nService {
  def get(page: String, languageCode: String) = {
    var i18n = mutable.Map.empty[String, String]

    if (page == "register") {
      i18n += (page + ".title" -> Messages(page + ".title")(Lang(languageCode)))
    }

    i18n
  }
}
