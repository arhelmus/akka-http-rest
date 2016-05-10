package me.archdev

import akka.http.scaladsl.model.{HttpEntity, MediaTypes, StatusCodes}
import me.archdev.restapi.models.{TokenEntity, UserEntity}

import io.circe.generic.auto._
import io.circe.syntax._

import scala.util.Try

class AuthServiceTest extends BaseServiceTest {
  val newUser = UserEntity(username = "NewUser", password = "test")
  var signUpToken: Option[TokenEntity] = None
  var signInToken: Option[TokenEntity] = None

  "Auth service" should {

    "register users and retrieve token" in {
      val requestEntity = HttpEntity(MediaTypes.`application/json`, newUser.asJson.noSpaces)
      Post("/auth/signUp", requestEntity) ~> httpService.authRouter.route ~> check {
        response.status should be(StatusCodes.Created)
        signUpToken = Try(responseAs[TokenEntity]).toOption
      }
    }

    "authorize users by login and password and retrieve token" in {
      val requestEntity = HttpEntity(
        MediaTypes.`application/json`,
        s"""{"login": "${newUser.username}", "password": "${newUser.password}"}"""
      )
      Post("/auth/signIn", requestEntity) ~> httpService.authRouter.route ~> check {
        signInToken = Try(responseAs[TokenEntity]).toOption
      }
    }

    "retrieve same tokens during registration and authorization" in {
      signUpToken should be(signInToken)
    }

  }

}
