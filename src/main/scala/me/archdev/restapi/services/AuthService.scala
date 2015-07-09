package me.archdev.restapi.services

import me.archdev.restapi.models.{ TokenEntity, UserEntity }

object AuthService extends AuthService

trait AuthService {

  val user = UserEntity(Some(1), "Arhelmus", "test")

  def signIn(login: String, password: String): Option[TokenEntity] = {
    if (login == "Arhelmus" && password == "test") {
      Some(TokenEntity(Some(1), "test"))
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
