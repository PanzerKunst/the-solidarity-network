package database

object DbUtil {
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
}
