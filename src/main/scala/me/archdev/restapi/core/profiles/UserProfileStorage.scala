package me.archdev.restapi.core.profiles

import me.archdev.restapi.core.UserProfile
import me.archdev.restapi.utils.db.DatabaseConnector

import scala.concurrent.{ExecutionContext, Future}

sealed trait UserProfileStorage {

  def getProfiles(): Future[Seq[UserProfile]]

  def getProfile(id: String): Future[Option[UserProfile]]

  def saveProfile(profile: UserProfile): Future[UserProfile]

}

class JdbcUserProfileStorage(
  val databaseConnector: DatabaseConnector
)(implicit executionContext: ExecutionContext) extends UserProfileTable with UserProfileStorage {

  import databaseConnector._
  import databaseConnector.profile.api._

  def getProfiles(): Future[Seq[UserProfile]] = db.run(profiles.result)

  def getProfile(id: String): Future[Option[UserProfile]] = db.run(profiles.filter(_.id === id).result.headOption)

  def saveProfile(profile: UserProfile): Future[UserProfile] =
    db.run((profiles returning profiles).insertOrUpdate(profile)).map(_ => profile)

}

class InMemoryUserProfileStorage extends UserProfileStorage {

  private var state: Seq[UserProfile] = Nil

  override def getProfiles(): Future[Seq[UserProfile]] =
    Future.successful(state)

  override def getProfile(id: String): Future[Option[UserProfile]] =
    Future.successful(state.find(_.id == id))

  override def saveProfile(profile: UserProfile): Future[UserProfile] =
    Future.successful {
      state = state.filterNot(_.id == profile.id)
      state = state :+ profile
      profile
    }

}