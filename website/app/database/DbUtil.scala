package database

import java.text.SimpleDateFormat
import java.util.Date

object DbUtil {
  private val dbDateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

  def generateWhereClause(filters: Option[Map[String, String]]) = {
    filters match {
      case Some(filtrs) => {
        filtrs.map {
          case (k, v) => """%s = '%s'""".format(k, backslashQuotes(v))
        }
          .mkString("where ", "\nand ", "")
      }
      case None => ""
    }
  }

  def backslashQuotes(string: String): String = {
    string.replaceAll("\"", "\\\\\"")
      .replaceAll("'", "\\\\'")
  }

  def dateToString(date: Date): String = {
    dbDateFormat.format(date)
  }
}
