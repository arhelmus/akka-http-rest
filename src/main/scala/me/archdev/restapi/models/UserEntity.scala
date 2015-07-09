package me.archdev.restapi.models

case class UserEntity(id: Option[Long] = None, username: String) {
  require(!username.isEmpty, "username.empty")
}
