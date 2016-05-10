package me.archdev

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import io.circe.generic.auto._
import me.archdev.restapi.models.UserEntity
import org.scalatest.concurrent.ScalaFutures

import scala.util.Random

class UsersServiceTest extends BaseServiceTest with ScalaFutures {

  import usersService._

  trait Context {
    val testUsers = provisionUsersList(5)
    val testTokens = provisionTokensForUsers(testUsers)
    val route = httpService.usersRouter.route
  }

  "Users service" should {

    "retrieve users list" in new Context {
      Get("/users") ~> route ~> check {
        responseAs[Seq[UserEntity]].isEmpty should be(false)
      }
    }

    "retrieve user by id" in new Context {
      val testUser = testUsers(4)
      Get(s"/users/${testUser.id.get}") ~> route ~> check {
        responseAs[UserEntity] should be(testUser)
      }
    }

    "update user by id and retrieve it" in new Context {
      val testUser = testUsers(3)
      val newUsername = Random.nextString(10)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, s"""{"username": "$newUsername"}""")

      Post(s"/users/${testUser.id.get}", requestEntity) ~> route ~> check {
        responseAs[UserEntity] should be(testUser.copy(username = newUsername))
        whenReady(getUserById(testUser.id.get)) { result =>
          result.get.username should be(newUsername)
        }
      }
    }

    "delete user" in new Context {
      val testUser = testUsers(2)
      Delete(s"/users/${testUser.id.get}") ~> route ~> check {
        response.status should be(NoContent)
        whenReady(getUserById(testUser.id.get)) { result =>
          result should be(None: Option[UserEntity])
        }
      }
    }

    "retrieve currently logged user" in new Context {
      val testUser = testUsers(1)
      val header = "Token" -> testTokens.find(_.userId.contains(testUser.id.get)).get.token

      Get("/users/me") ~> addHeader(header._1, header._2) ~> route ~> check {
        responseAs[UserEntity] should be(testUsers.find(_.id.contains(testUser.id.get)).get)
      }
    }

    "update currently logged user" in new Context {
      val testUser = testUsers.head
      val newUsername = Random.nextString(10)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, s"""{"username": "$newUsername"}""")
      val header = "Token" -> testTokens.find(_.userId.contains(testUser.id.get)).get.token

      Post("/users/me", requestEntity) ~> addHeader(header._1, header._2) ~> route ~> check {
        responseAs[UserEntity] should be(testUsers.find(_.id.contains(testUser.id.get)).get.copy(username = newUsername))
        whenReady(getUserById(testUser.id.get)) { result =>
          result.get.username should be(newUsername)
        }
      }
    }

  }

}
