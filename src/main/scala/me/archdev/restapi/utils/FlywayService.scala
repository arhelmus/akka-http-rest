package me.archdev.restapi.utils

import org.flywaydb.core.Flyway

class FlywayService(jdbcUrl: String, dbUser: String, dbPassword: String) {

  private val flyway = new Flyway()
  flyway.setDataSource(jdbcUrl, dbUser, dbPassword)

  def migrateDatabaseSchema = {
    flyway.migrate()
    this
  }

  def dropDatabase = {
    flyway.clean()
    this
  }

}
