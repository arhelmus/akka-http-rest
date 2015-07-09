package me.archdev.restapi.models

case class UserEntity(id: Option[Long] = None, username: String) {
  require(!username.isEmpty, "username.empty")
}

case class UserEntityUpdate(id: Option[Long] = None, username: Option[String] = None) {
  def merge(user: UserEntity): UserEntity = {
    UserEntity(user.id, username.getOrElse(user.username))
  }
}