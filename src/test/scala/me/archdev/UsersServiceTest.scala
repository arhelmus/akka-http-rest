package me.archdev

import akka.http.scaladsl.model.{ HttpEntity, MediaTypes }
import me.archdev.restapi.http.routes.UsersServiceRoute
import me.archdev.restapi.models.UserEntity
import org.scalatest.concurrent.ScalaFutures

import spray.json._
import akka.http.scaladsl.model.StatusCodes._

class UsersServiceTest extends BaseServiceTest with ScalaFutures {
  "Users service" should {
    "retrieve users list" in {
      Get("/users") ~> usersRoute ~> check {
        responseAs[JsArray] should be(testUsers.toJson)
      }
    }

    "retrieve user by id" in {
      Get("/users/1") ~> usersRoute ~> check {
        responseAs[JsObject] should be(testUsers.head.toJson)
      }
    }

    "update user by id and retrieve it" in {
      val newUsername = "UpdatedUsername"
      val requestEntity = HttpEntity(MediaTypes.`application/json`, JsObject("username" -> JsString(newUsername)).toString())
      Post("/users/1", requestEntity) ~> usersRoute ~> check {
        responseAs[JsObject] should be(testUsers.head.copy(username = newUsername).toJson)
        whenReady(getUserById(1)) { result =>
          result.get.username should be(newUsername)
        }
      }
    }

    "delete user" in {
      Delete("/users/3") ~> usersRoute ~> check {
        response.status should be(NoContent)
        whenReady(getUserById(3)) { result =>
          result should be(None: Option[UserEntity])
        }
      }
    }

    "retrieve currently logged user" in {
      Get("/users/me") ~> addHeader("Token", testTokens.find(_.userId.contains(2)).get.token) ~> usersRoute ~> check {
        responseAs[JsObject] should be(testUsers.find(_.id.contains(2)).get.toJson)
      }
    }

    "update currently logged user" in {
      val newUsername = "MeUpdatedUsername"
      val requestEntity = HttpEntity(MediaTypes.`application/json`, JsObject("username" -> JsString(newUsername)).toString())
      Post("/users/me", requestEntity) ~> addHeader("Token", testTokens.find(_.userId.contains(2)).get.token) ~> usersRoute ~> check {
        responseAs[JsObject] should be(testUsers.find(_.id.contains(2)).get.copy(username = newUsername).toJson)
        whenReady(getUserById(2)) { result =>
          result.get.username should be(newUsername)
        }
      }
    }
  }

}
