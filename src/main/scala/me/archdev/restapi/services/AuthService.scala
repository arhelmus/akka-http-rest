package me.archdev.restapi.services

import me.archdev.restapi.models.db.TokenEntityTable
import me.archdev.restapi.models.{TokenEntity, UserEntity}
import me.archdev.restapi.utils.DatabaseService

import scala.concurrent.{ExecutionContext, Future}

class AuthService(val databaseService: DatabaseService)(usersService: UsersService)(implicit executionContext: ExecutionContext) extends TokenEntityTable {

  import databaseService._
  import databaseService.profile.api._

  def signIn(login: String, password: String): Future[Option[TokenEntity]] = {
    db.run(users.filter(u => u.username === login).result).flatMap { users =>
      users.find(user => user.password == password) match {
        case Some(user) => db.run(tokens.filter(_.userId === user.id).result.headOption).flatMap {
          case Some(token) => Future.successful(Some(token))
          case None        => createToken(user).map(token => Some(token))
        }
        case None => Future.successful(None)
      }
    }
  }

  def signUp(newUser: UserEntity): Future[TokenEntity] = {
    usersService.createUser(newUser).flatMap(user => createToken(user))
  }

  def authenticate(token: String): Future[Option[UserEntity]] =
    db.run((for {
      token <- tokens.filter(_.token === token)
      user <- users.filter(_.id === token.userId)
    } yield user).result.headOption)

  def createToken(user: UserEntity): Future[TokenEntity] = db.run(tokens returning tokens += TokenEntity(userId = user.id))

}
