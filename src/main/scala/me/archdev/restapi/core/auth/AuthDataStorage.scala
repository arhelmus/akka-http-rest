package me.archdev.restapi.core.auth

import me.archdev.restapi.core.AuthData
import me.archdev.restapi.utils.db.DatabaseConnector
import me.archdev.restapi.core.profiles.UserProfileStorage

import scala.concurrent.{ExecutionContext, Future}

sealed trait AuthDataStorage {

  def findAuthData(login: String): Future[Option[AuthData]]

  def saveAuthData(authData: AuthData): Future[AuthData]

}

class JdbcAuthDataStorage(
  val databaseConnector: DatabaseConnector
)(implicit executionContext: ExecutionContext) extends AuthDataTable with AuthDataStorage {

  import databaseConnector._
  import databaseConnector.profile.api._

  override def findAuthData(login: String): Future[Option[AuthData]] =
    db.run(auth.filter(d => d.username === login || d.email === login).result.headOption)

  override def saveAuthData(authData: AuthData): Future[AuthData] =
    db.run(auth.insertOrUpdate(authData)).map(_ => authData)

}

class InMemoryAuthDataStorage extends AuthDataStorage {

  private var state: Seq[AuthData] = Nil

  override def findAuthData(login: String): Future[Option[AuthData]] =
    Future.successful(state.find(d => d.username == login || d.email == login))

  override def saveAuthData(authData: AuthData): Future[AuthData] =
    Future.successful {
      state = state :+ authData
      authData
    }

}