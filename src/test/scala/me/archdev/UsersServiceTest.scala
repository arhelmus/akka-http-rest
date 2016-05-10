package me.archdev

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes}

import me.archdev.restapi.models.UserEntity
import org.scalatest.concurrent.ScalaFutures

import io.circe.Json
import io.circe.generic.auto._
import io.circe.syntax._

class UsersServiceTest extends BaseServiceTest with ScalaFutures {

  import usersService._

  "Users service" should {

    "retrieve users list" in {
      Get("/users") ~> httpService.usersRouter.route ~> check {
        responseAs[Json] should be(testUsers.asJson)
      }
    }

    "retrieve user by id" in {
      Get("/users/1") ~> httpService.usersRouter.route ~> check {
        responseAs[Json] should be(testUsers.head.asJson)
      }
    }

    "update user by id and retrieve it" in {
      val newUsername = "UpdatedUsername"
      val requestEntity = HttpEntity(MediaTypes.`application/json`, s"""{"username": "$newUsername"}""")
      Post("/users/1", requestEntity) ~> httpService.usersRouter.route ~> check {
        responseAs[Json] should be(testUsers.head.copy(username = newUsername).asJson)
        whenReady(getUserById(1)) { result =>
          result.get.username should be(newUsername)
        }
      }
    }

    "delete user" in {
      Delete("/users/3") ~> httpService.usersRouter.route ~> check {
        response.status should be(NoContent)
        whenReady(getUserById(3)) { result =>
          result should be(None: Option[UserEntity])
        }
      }
    }

    "retrieve currently logged user" in {
      Get("/users/me") ~> addHeader("Token", testTokens.find(_.userId.contains(2)).get.token) ~> httpService.usersRouter.route ~> check {
        responseAs[Json] should be(testUsers.find(_.id.contains(2)).get.asJson)
      }
    }

    "update currently logged user" in {
      val newUsername = "MeUpdatedUsername"
      val requestEntity = HttpEntity(MediaTypes.`application/json`, s"""{"username": "$newUsername"}""")
      Post("/users/me", requestEntity) ~> addHeader("Token", testTokens.find(_.userId.contains(2)).get.token) ~> httpService.usersRouter.route ~> check {
        responseAs[Json] should be(testUsers.find(_.id.contains(2)).get.copy(username = newUsername).asJson)
        whenReady(getUserById(2)) { result =>
          result.get.username should be(newUsername)
        }
      }
    }

  }

}
