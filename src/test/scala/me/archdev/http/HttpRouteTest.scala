package me.archdev.http

import akka.http.scaladsl.server.Route
import me.archdev.BaseServiceTest
import me.archdev.restapi.core.auth.AuthService
import me.archdev.restapi.core.profiles.UserProfileService
import me.archdev.restapi.http.HttpRoute

class HttpRouteTest extends BaseServiceTest {

  "HttpRoute" when {

    "GET /healthcheck" should {

      "return 200 OK" in new Context {
        Get("/healthcheck") ~> httpRoute ~> check {
          responseAs[String] shouldBe "OK"
          status.intValue() shouldBe 200
        }
      }

    }

  }

  trait Context {
    val secretKey = "secret"
    val userProfileService: UserProfileService = mock[UserProfileService]
    val authService: AuthService = mock[AuthService]

    val httpRoute: Route = new HttpRoute(userProfileService, authService, secretKey).route
  }

}
