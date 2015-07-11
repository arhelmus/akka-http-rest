package me.archdev.restapi.models.db

import me.archdev.restapi.models.UserEntity
import me.archdev.restapi.utils.DatabaseConfig

trait UserEntityTable extends DatabaseConfig {

  import driver.api._

  class Users(tag: Tag) extends Table[UserEntity](tag, "users") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def username = column[String]("username")
    def password = column[String]("password")

    def * = (id, username, password) <> ((UserEntity.apply _).tupled, UserEntity.unapply)
  }

  protected val users = TableQuery[Users]

}
