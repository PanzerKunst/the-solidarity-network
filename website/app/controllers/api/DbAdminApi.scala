package controllers.api

import play.api.mvc.{Action, Controller}
import db.DbAdmin

object DbAdminApi extends Controller {
  def createTables = Action {
    implicit request =>

      if (request.queryString.contains("key") &&
        request.queryString.get("key").get.head == "OZs:]T@R]u9I4nyqbvNyAMe4hPZaoFsNhiQvGjCh@GErJ7/0wqaVdj8To8MpmE1O") {
        DbAdmin.createTables()
        DbAdmin.initData()
        Ok
      }
      else
        Forbidden
  }

  def dropTables = Action {
    implicit request =>

      if (request.queryString.contains("key") &&
        request.queryString.get("key").get.head == "OZs:]T@R]u9I4nyqbvNyAMe4hPZaoFsNhiQvGjCh@GErJ7/0wqaVdj8To8MpmE1O") {
        DbAdmin.dropTables()
        Ok
      }
      else
        Forbidden
  }
}
