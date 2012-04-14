package database

import java.text.SimpleDateFormat
import java.util.Date

object DbUtil {
  private val dbDateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

  def generateWhereClause(filters: Map[String, String]) = {
    if (filters.size > 0)
      filters.map {
        case (k, v) => """%s = '%s'""".format(k, backslashQuotes(v))
      }
        .mkString("where ", "\nand ", "")
    else
      ""
  }

  def backslashQuotes(string: String): String = {
    string.replaceAll("\"", "\\\\\"")
      .replaceAll("'", "\\\\'")
  }

  def dateToString(date: Date): String = {
    dbDateFormat.format(date)
  }
}
