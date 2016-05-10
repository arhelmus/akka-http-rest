package me.archdev.restapi.utils

class DatabaseService(jdbcUrl: String, dbUser: String, dbPassword: String) {
  val driver = slick.driver.PostgresDriver

  import driver.api._
  val db = Database.forURL(jdbcUrl, dbUser, dbPassword, driver = "org.postgresql.Driver")
  db.createSession()
}
