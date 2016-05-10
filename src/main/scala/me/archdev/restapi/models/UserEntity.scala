package me.archdev.restapi.models

case class UserEntity(id: Option[Long] = None, username: String, password: String) {
  require(!username.isEmpty, "username.empty")
  require(!password.isEmpty, "password.empty")
}

case class UserEntityUpdate(username: Option[String] = None, password: Option[String] = None) {
  def merge(user: UserEntity): UserEntity = {
    UserEntity(user.id, username.getOrElse(user.username), password.getOrElse(user.password))
  }
}