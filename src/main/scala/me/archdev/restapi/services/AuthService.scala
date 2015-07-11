package me.archdev.restapi.services

import me.archdev.restapi.models.db.TokenEntityTable
import me.archdev.restapi.models.{ TokenEntity, UserEntity }
import org.mindrot.jbcrypt.BCrypt

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object AuthService extends AuthService

trait AuthService extends TokenEntityTable {

  import driver.api._

  def signIn(login: String, password: String): Future[Option[TokenEntity]] = {
    db.run(users.filter(u => u.username === login).result).flatMap { users =>
      users.find(user => BCrypt.checkpw(password, user.password)) match {
        case Some(user) => db.run(tokens.filter(_.userId === user.id).result.headOption).flatMap {
          case Some(token) => Future.successful(Some(token))
          case None        => createToken(user).map(token => Some(token))
        }
        case None => Future.successful(None)
      }
    }
  }

  def signUp(newUser: UserEntity): Future[TokenEntity] = {
    UsersService.createUser(newUser).flatMap(user => createToken(user))
  }

  def authenticate(token: String): Future[Option[UserEntity]] =
    db.run((for {
      token <- tokens.filter(_.token === token)
      user <- users.filter(_.id === token.userId)
    } yield user).result.headOption)

  def createToken(user: UserEntity): Future[TokenEntity] = db.run(tokens returning tokens += TokenEntity(userId = user.id))

}
