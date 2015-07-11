package me.archdev.restapi.utils

trait DatabaseConfig {
  val driver = slick.driver.PostgresDriver

  import driver.api._

  def db = Database.forConfig("database")

  implicit val session: Session = db.createSession()
}
