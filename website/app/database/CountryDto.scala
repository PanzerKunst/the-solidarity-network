package database

import anorm._
import play.api.db.DB
import play.api.Play.current
import models.Country
import play.api.Logger


object CountryDto {
  val all = getAll

  def getAll: List[Country] = {

    DB.withConnection {
      implicit c =>

        val query = """
          select id, name
          from country
          order by name;
                    """

        Logger.info("CountryDto.getAll(): " + query)

        val sql = SQL(query)

        sql().map(row =>
          new Country(
            row[Long]("id"),
            row[String]("name")
          )
        ) toList
    }
  }

  def getOfId(id: Long) = {
    all.filter(c => c.id == id)
  }
}
