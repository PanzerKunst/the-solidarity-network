package database

import anorm._
import play.api.db.DB
import models.{User, HelpRequest}
import play.api.Play.current
import play.api.Logger
import java.util

object HelpRequestDto {
  def get(filters: Option[Map[String, String]]): List[HelpRequest] = {
    DB.withConnection {
      implicit c =>

        val query = """
          select id, requester_id, title, description, creation_date, expiry_date
          from help_request """ + DbUtil.generateWhereClause(filters) + """
          order by expiry_date, creation_date;"""

        Logger.info("HelpRequestDto.get():" + query)

        SQL(query)().map(row =>
          HelpRequest(
            id = Some(row[Long]("id")),
            requesterId = Some(row[Long]("requester_id")),
            title = row[String]("title"),
            description = row[String]("description"),
            creationDatetime = Some(row[util.Date]("creation_date")),
            expiryDate = row[util.Date]("expiry_date")
          )
        ).toList
    }
  }

  def searchGeneric(searchQuery: Option[String], exceptFromRequesterId: Option[Long] = None): List[(HelpRequest, User)] = {
    DB.withConnection {
      implicit c =>

        val ownFilteredClause = exceptFromRequesterId match {
          case Some(requesterId) => " and hr.requester_id <> " + requesterId
          case None => ""
        }

        val query = searchQuery match {
          case Some(sq) =>
            val processedSearchQuery = DbUtil.backslashQuotes(sq.replaceAll("\\*", "%"))

            """
            select distinct hr.id, hr.title, hr.description, hr.creation_date, hr.expiry_date,
              u.id, u.first_name, u.last_name, u.username, u.email, u.city, u.country_id,
              c.id, c.name
            from help_request hr
            inner join user u on u.id = hr.requester_id
            inner join country c on c.id = u.country_id
            where hr.title like """" + processedSearchQuery + """"
            or hr.description like """" + processedSearchQuery + """"
            or u.first_name like """" + processedSearchQuery + """"
            or u.last_name like """" + processedSearchQuery + """"
            or u.username like """" + processedSearchQuery + """"
            or u.city like """" + processedSearchQuery + """"
            or c.name like """" + processedSearchQuery + """"""" +
              ownFilteredClause + """
            order by expiry_date, creation_date
            limit 50;"""

          case None => """
            select hr.id, hr.title, hr.description, hr.creation_date, hr.expiry_date,
              u.id, u.first_name, u.last_name, u.username, u.email, u.city, u.country_id
            from help_request hr
            inner join user u on u.id = hr.requester_id""" +
              ownFilteredClause + """
            order by expiry_date, creation_date
            limit 50;"""
        }

        Logger.info("HelpRequestDto.searchGeneric():" + query)

        SQL(query)().map(row =>
          (
            HelpRequest(
              id = Some(row[Long]("help_request.id")),
              requesterId = Some(row[Long]("user.id")),
              title = row[String]("title"),
              description = row[String]("description"),
              creationDatetime = Some(row[util.Date]("creation_date")),
              expiryDate = row[util.Date]("expiry_date")
            ),
            User(
              id = Some(row[Long]("user.id")),
              firstName = Some(row[String]("first_name")),
              lastName = Some(row[String]("last_name")),
              username = Some(row[String]("username")),
              email = Some(row[String]("email")),
              city = Some(row[String]("city")),
              countryId = Some(row[Long]("country_id"))
            )
            )
        ).toList
    }
  }

  def searchAdvanced(filters: Map[String, String], exceptFromRequesterId: Option[Long] = None): List[(HelpRequest, User)] = {
    DB.withConnection {
      implicit c =>

        val innerJoinForRepliedBy = if (filters.contains("repliedBy"))
          "inner join help_reply r on hr.id = r.request_id"
        else ""

        val ownFilteredClause = exceptFromRequesterId match {
          case Some(requesterId) => " and hr.requester_id <> " + requesterId
          case None => ""
        }

        val query = """
            select distinct hr.id, hr.title, hr.description, hr.creation_date, hr.expiry_date,
              u.id, u.first_name, u.last_name, u.username, u.email, u.city, u.country_id,
              c.id, c.name
            from help_request hr
            inner join user u on u.id = hr.requester_id
            inner join country c on c.id = u.country_id
            """ + innerJoinForRepliedBy +
              DbUtil.generateWhereClause(Some(webFiltersToDbFilters(filters))) +
              ownFilteredClause + """
            order by expiry_date, creation_date
            limit 50;"""

        Logger.info("HelpRequestDto.searchAdvanced():" + query)

        SQL(query)().map(row =>
          (
            HelpRequest(
              id = Some(row[Long]("help_request.id")),
              requesterId = Some(row[Long]("user.id")),
              title = row[String]("title"),
              description = row[String]("description"),
              creationDatetime = Some(row[util.Date]("creation_date")),
              expiryDate = row[util.Date]("expiry_date")
            ),
            User(
              id = Some(row[Long]("user.id")),
              firstName = Some(row[String]("first_name")),
              lastName = Some(row[String]("last_name")),
              username = Some(row[String]("username")),
              email = Some(row[String]("email")),
              city = Some(row[String]("city")),
              countryId = Some(row[Long]("country_id"))
            )
            )
        ).toList
    }
  }

  def create(helpRequest: HelpRequest): Option[Long] = {
    DB.withConnection {
      implicit c =>

        val descriptionForQuery = helpRequest.description.replaceAll("\n", "\\\\n");

        val query = """
                       insert into help_request(requester_id, title, description, creation_date, expiry_date)
      values(""" + helpRequest.requesterId.get + """, """" +
          DbUtil.backslashQuotes(helpRequest.title) + """", """" +
          DbUtil.backslashQuotes(descriptionForQuery) + """", """" +
          DbUtil.datetimeToString(new util.Date()) + """", """" +
          DbUtil.dateToString(helpRequest.expiryDate) + """");"""

        Logger.info("HelpRequestDto.create(): " + query)

        SQL(query).executeInsert()
    }
  }

  def update(helpRequest: HelpRequest) {
    DB.withConnection {
      implicit c =>

        val descriptionForQuery = helpRequest.description.replaceAll("\n", "\\\\n");

        val query = """
                       update help_request set
          title = """" + DbUtil.backslashQuotes(helpRequest.title) + """",
          description = """" + DbUtil.backslashQuotes(descriptionForQuery) + """",
          expiry_date = """" + DbUtil.dateToString(helpRequest.expiryDate) + """"
          where id = """ + helpRequest.id.get + """;"""

        Logger.info("HelpRequestDto.update():" + query)

        SQL(query).execute()
    }
  }

  def delete(helpRequest: HelpRequest) {
    DB.withConnection {
      implicit c =>

        val query = """
                      delete from help_request
          where id = """ + helpRequest.id.get + """;"""

        Logger.info("HelpRequestDto.delete():" + query)

        SQL(query).execute()
    }
  }

  private def webFiltersToDbFilters(webFilters: Map[String, String]): Map[String, String] = {
    var dbFilters: Map[String, String] = Map()
    for (key <- webFilters.keys)
      if (key == "username") {
        val rawFilterValue = webFilters.get(key).get
        val processedFilterValue = rawFilterValue.replaceAll("\\*", "%")
        dbFilters += ("u.username" -> processedFilterValue)
      }
      else if (key == "firstName") {
        val rawFilterValue = webFilters.get(key).get
        val processedFilterValue = rawFilterValue.replaceAll("\\*", "%")
        dbFilters += ("u.first_name" -> processedFilterValue)
      }
      else if (key == "lastName") {
        val rawFilterValue = webFilters.get(key).get
        val processedFilterValue = rawFilterValue.replaceAll("\\*", "%")
        dbFilters += ("u.last_name" -> processedFilterValue)
      }
      else if (key == "city") {
        val rawFilterValue = webFilters.get(key).get
        val processedFilterValue = rawFilterValue.replaceAll("\\*", "%")
        dbFilters += ("u.city" -> processedFilterValue)
      }
      else if (key == "country") {
        val rawFilterValue = webFilters.get(key).get
        val processedFilterValue = rawFilterValue.replaceAll("\\*", "%")
        dbFilters += ("c.name" -> processedFilterValue)
      }
      else if (key == "repliedBy") {
        val rawFilterValue = webFilters.get(key).get
        val user = UserDto.get(Some(Map("username" -> rawFilterValue))).head
        dbFilters += ("r.replier_id" -> user.id.get.toString)
      }

    dbFilters
  }
}
