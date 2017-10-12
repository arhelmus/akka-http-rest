package me.archdev.http.routes

import java.util.UUID

import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Route
import io.circe.generic.auto._
import io.circe.syntax._
import me.archdev.BaseServiceTest
import me.archdev.restapi.core.{AuthTokenContent, UserProfile, UserProfileUpdate}
import me.archdev.restapi.core.profiles.UserProfileService
import me.archdev.restapi.http.routes.ProfileRoute
import org.mockito.Mockito.when
import pdi.jwt.{Jwt, JwtAlgorithm}

import scala.concurrent.Future
import scala.util.Random

class ProfileRouteTest extends BaseServiceTest {

  "ProfileRoute" when {

    "GET /profiles" should {

      "return 200 and all profiles JSON" in new Context {
        when(userProfileService.getProfiles()).thenReturn(Future.successful(Seq(testProfile1, testProfile2)))

        Get("/profiles") ~> profileRoute ~> check {
          responseAs[String] shouldBe Seq(testProfile1, testProfile2).asJson.noSpaces
          status.intValue() shouldBe 200
        }
      }

    }

    "GET /profiles/me with Token header" should {

      "return 200 and current user profile JSON" in new Context {
        when(userProfileService.getProfile(testProfile1.id)).thenReturn(Future.successful(Some(testProfile1)))
        val header = RawHeader("Token", buildAuthToken(testProfile1.id))

        Get("/profiles/me").withHeaders(header) ~> profileRoute ~> check {
          responseAs[String] shouldBe testProfile1.asJson.noSpaces
          status.intValue() shouldBe 200
        }
      }

      "return 500 if token is incorrect" in new Context {
        val header = RawHeader("Token", "token")

        Get("/profiles/me").withHeaders(header) ~> profileRoute ~> check {
          status.intValue() shouldBe 500
        }
      }

    }

    "POST /profiles/me" should {

      "update profile and return 200" in new Context {
        when(userProfileService.updateProfile(testProfile1.id, UserProfileUpdate(Some(""), Some("")))).thenReturn(Future.successful(Some(testProfile1)))
        val header = RawHeader("Token", buildAuthToken(testProfile1.id))
        val requestEntity = HttpEntity(MediaTypes.`application/json`, s"""{"firstName": "", "lastName": ""}""")

        Post("/profiles/me", requestEntity).withHeaders(header) ~> profileRoute ~> check {
          responseAs[String] shouldBe testProfile1.asJson.noSpaces
          status.intValue() shouldBe 200
        }
      }

      "return 500 if token is incorrect" in new Context {
        val header = RawHeader("Token", "token")
        val requestEntity = HttpEntity(MediaTypes.`application/json`, s"""{"firstName": "", "lastName": ""}""")

        Post("/profiles/me", requestEntity).withHeaders(header) ~> profileRoute ~> check {
          status.intValue() shouldBe 500
        }
      }

    }

    "GET /profiles/:id" should {

      "return 200 and user profile JSON" in new Context {
        when(userProfileService.getProfile(testProfile1.id)).thenReturn(Future.successful(Some(testProfile1)))

        Get("/profiles/" + testProfileId1) ~> profileRoute ~> check {
          responseAs[String] shouldBe testProfile1.asJson.noSpaces
          status.intValue() shouldBe 200
        }
      }

      "return 400 if profile not exists" in new Context {
        when(userProfileService.getProfile(testProfile1.id)).thenReturn(Future.successful(None))

        Get("/profiles/" + testProfileId1) ~> profileRoute ~> check {
          status.intValue() shouldBe 400
        }
      }

    }

    "POST /profiles/:id" should {

      "update user profile and return 200" in new Context {
        when(userProfileService.updateProfile(testProfile1.id, UserProfileUpdate(Some(""), Some("")))).thenReturn(Future.successful(Some(testProfile1)))
        val requestEntity = HttpEntity(MediaTypes.`application/json`, s"""{"firstName": "", "lastName": ""}""")

        Post("/profiles/" + testProfileId1, requestEntity) ~> profileRoute ~> check {
          responseAs[String] shouldBe testProfile1.asJson.noSpaces
          status.intValue() shouldBe 200
        }
      }

      "return 400 if profile not exists" in new Context {
        when(userProfileService.updateProfile(testProfile1.id, UserProfileUpdate(Some(""), Some("")))).thenReturn(Future.successful(None))
        val requestEntity = HttpEntity(MediaTypes.`application/json`, s"""{"firstName": "", "lastName": ""}""")

        Post("/profiles/" + testProfileId1, requestEntity) ~> profileRoute ~> check {
          status.intValue() shouldBe 400
        }
      }

    }

  }

  trait Context {
    val secretKey = "secret"
    val userProfileService: UserProfileService = mock[UserProfileService]
    val profileRoute: Route = new ProfileRoute(secretKey, userProfileService).route

    val testProfileId1: String = UUID.randomUUID().toString
    val testProfileId2: String = UUID.randomUUID().toString
    val testProfile1: UserProfile = testProfile(testProfileId1)
    val testProfile2: UserProfile = testProfile(testProfileId2)

    def testProfile(id: String) = UserProfile(id, Random.nextString(10), Random.nextString(10))

    def buildAuthToken(id: String): String = Jwt.encode(AuthTokenContent(id).asJson.noSpaces, secretKey, JwtAlgorithm.HS256)
  }

}
