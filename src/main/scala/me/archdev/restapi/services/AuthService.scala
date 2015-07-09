package me.archdev.restapi.services

import me.archdev.restapi.models.UserEntity

object AuthService extends AuthService

trait AuthService {

  val user = UserEntity(Some(1), "Arhelmus", "test")

  def signIn(login: String, password: String): Option[UserEntity] = {
    if (login == "Arhelmus" && password == "test") {
      Some(user)
    } else {
      None
    }
  }

  def authenticate(token: String): Option[UserEntity] = {
    if (token == "test") {
      Some(user)
    } else {
      None
    }
  }

}
