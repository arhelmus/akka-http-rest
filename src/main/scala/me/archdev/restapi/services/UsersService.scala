package me.archdev.restapi.services

import me.archdev.restapi.models.UserEntity

import scala.collection.{ immutable, mutable }

object UsersService extends UsersService

trait UsersService {

  val users = mutable.Set(
    UserEntity(Some(1), "Arhelmus"),
    UserEntity(Some(2), "Tetsu")
  )

  def getUsers(): immutable.Set[UserEntity] = users.toSet

  def getUserById(id: Long): Option[UserEntity] = users.find(_.id.get == id)

  def getUserByLogin(login: String): Option[UserEntity] = users.find(_.username == login)

  def saveUser(user: UserEntity): UserEntity = {
    user.id match {
      case Some(id) =>
        users -= users.find(_.id.get == id).get
        users += user
        user
      case None =>
        val newUser = user.copy(id = Some(users.maxBy(_.id.get).id.get + 1))
        users += newUser
        newUser
    }
  }

  def deleteUser(id: Long): Boolean = {
    users -= users.find(_.id.get == id).get
    true
  }

}