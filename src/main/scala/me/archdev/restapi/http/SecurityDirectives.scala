package me.archdev.restapi.http

import akka.http.scaladsl.server.directives.{ RouteDirectives, BasicDirectives, HeaderDirectives }
import akka.http.scaladsl.server.Directive1
import me.archdev.restapi.models.UserEntity
import me.archdev.restapi.services.AuthService

trait SecurityDirectives {

  import BasicDirectives._
  import HeaderDirectives._
  import RouteDirectives._

  def authenticate: Directive1[UserEntity] = {
    headerValueByName("Token").flatMap { token =>
      AuthService.authenticate(token) match {
        case Some(user) => provide(user)
        case None       => reject
      }
    }
  }

}
