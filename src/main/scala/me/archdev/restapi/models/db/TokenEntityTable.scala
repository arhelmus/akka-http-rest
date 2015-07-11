package me.archdev.restapi.models.db

import me.archdev.restapi.models.TokenEntity
import me.archdev.restapi.utils.DatabaseConfig

trait TokenEntityTable extends UserEntityTable with DatabaseConfig {

  import driver.api._

  class Tokens(tag: Tag) extends Table[TokenEntity](tag, "tokens") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def userId = column[Option[Long]]("user_id")
    def token = column[String]("token")

    def userFk = foreignKey("USER_FK", userId, users)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

    def * = (id, userId, token) <> ((TokenEntity.apply _).tupled, TokenEntity.unapply)
  }

  protected val tokens = TableQuery[Tokens]

}
