package me.archdev

import akka.http.scaladsl.model.{HttpEntity, MediaTypes, StatusCodes}
import akka.http.scaladsl.server
import io.circe.generic.auto._
import io.circe.syntax._
import me.archdev.restapi.models.{TokenEntity, UserEntity}

class AuthServiceTest extends BaseServiceTest {

  trait Context {
    val testUsers = provisionUsersList(2)
    val route = httpService.authRouter.route
  }

  "Auth service" should {

    "register users and retrieve token" in new Context {
      val testUser = testUsers(0)
      signUpUser(testUser, route) {
        response.status should be(StatusCodes.Created)
      }
    }

    "authorize users by login and password and retrieve token" in new Context {
      val testUser = testUsers(1)
      signInUser(testUser, route) {
        responseAs[TokenEntity] should be
      }
    }

  }

  private def signUpUser(user: UserEntity, route: server.Route)(action: => Unit) = {
    val requestEntity = HttpEntity(MediaTypes.`application/json`, user.asJson.noSpaces)
    Post("/auth/signUp", requestEntity) ~> route ~> check(action)
  }

  private def signInUser(user: UserEntity, route: server.Route)(action: => Unit) = {
    val requestEntity = HttpEntity(
      MediaTypes.`application/json`,
      s"""{"login": "${user.username}", "password": "${user.password}"}"""
    )
    Post("/auth/signIn", requestEntity) ~> route ~> check(action)
  }

}
