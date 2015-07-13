package me.archdev

import akka.http.scaladsl.model.{ StatusCodes, MediaTypes, HttpEntity }
import me.archdev.restapi.http.routes.AuthServiceRoute
import me.archdev.restapi.models.{ TokenEntity, UserEntity }
import spray.json._

class AuthServiceTest extends BaseServiceTest {
  val newUser = UserEntity(username = "NewUser", password = "test")
  var signUpToken: Option[TokenEntity] = None
  var signInToken: Option[TokenEntity] = None

  "Auth service" should {
    "register users and retrieve token" in {
      val requestEntity = HttpEntity(MediaTypes.`application/json`, newUser.toJson.toString())
      Post("/auth/signUp", requestEntity) ~> authRoute ~> check {
        response.status should be(StatusCodes.Created)
        signUpToken = Some(tokenFormat.read(responseAs[JsValue]))
      }
    }

    "authorize users by login and password and retrieve token" in {
      val requestEntity = HttpEntity(MediaTypes.`application/json`, JsObject("login" -> JsString(newUser.username), "password" -> JsString(newUser.password)).toString())
      Post("/auth/signIn", requestEntity) ~> authRoute ~> check {
        signInToken = Some(tokenFormat.read(responseAs[JsValue]))
      }
    }

    "retrieve same tokens during registration and authorization" in {
      signUpToken should be(signInToken)
    }
  }

}
