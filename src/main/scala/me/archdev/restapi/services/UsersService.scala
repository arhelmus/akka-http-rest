package me.archdev.restapi.services

import me.archdev.restapi.models.{ UserEntityUpdate, UserEntity }

import scala.collection.{ immutable, mutable }

object UsersService extends UsersService

trait UsersService {

  val users = mutable.Set(
    UserEntity(Some(1), "Arhelmus", "password"),
    UserEntity(Some(2), "Tetsu", "password")
  )

  def getUsers(): immutable.Set[UserEntity] = users.toSet

  def getUserById(id: Long): Option[UserEntity] = users.find(_.id.get == id)

  def getUserByLogin(login: String): Option[UserEntity] = users.find(_.username == login)

  def createUser(user: UserEntity): UserEntity = {
    val newUser = user.copy(id = Some(users.maxBy(_.id.get).id.get + 1))
    users += newUser
    newUser
  }

  def updateUser(id: Long, userUpdate: UserEntityUpdate): UserEntity = {
    val oldUser = users.find(_.id.get == id).get
    val newUser = userUpdate.merge(oldUser)
    users -= oldUser
    users += newUser
    newUser
  }

  def deleteUser(id: Long): Boolean = {
    users -= users.find(_.id.get == id).get
    true
  }

}