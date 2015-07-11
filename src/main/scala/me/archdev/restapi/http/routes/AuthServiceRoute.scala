package me.archdev.restapi.http.routes

import akka.http.scaladsl.server.Directives._
import me.archdev.restapi.http.{ SecurityDirectives, BaseService }
import me.archdev.restapi.services.AuthService
import spray.json._

trait AuthServiceRoute extends AuthService with BaseService with SecurityDirectives {

  case class LoginPassword(login: String, password: String)

  implicit val loginPasswordFormat = jsonFormat2(LoginPassword)

  val authRoute = pathPrefix("auth") {
    path("signIn") {
      pathEndOrSingleSlash {
        post {
          entity(as[LoginPassword]) { loginPassword =>
            complete(signIn(loginPassword.login, loginPassword.password).map(_.toJson))
          }
        }
      }
    }
  }

}
