package me.archdev.core.auth

import java.util.UUID

import com.roundeights.hasher.Implicits._
import me.archdev.BaseServiceTest
import me.archdev.restapi.core.AuthData
import me.archdev.restapi.core.auth.{AuthService, InMemoryAuthDataStorage}
import pdi.jwt.{Jwt, JwtAlgorithm}

import scala.util.Random

class AuthServiceTest extends BaseServiceTest {

  "AuthServiceTest" when {

    "signIn" should {

      "return valid auth token" in new Context {
        awaitForResult(for {
          _ <- authDataStorage.saveAuthData(testAuthData)
          Some(token) <- authService.signIn(testUsername, testPassword)
        } yield Jwt.decodeRaw(token, secretKey, Seq(JwtAlgorithm.HS256)).isSuccess shouldBe true)
      }

      "return None if password is incorrect" in new Context {
        awaitForResult(for {
          _ <- authDataStorage.saveAuthData(testAuthData)
          result <- authService.signIn(testUsername, "wrongpassword")
        } yield result shouldBe None)
      }

      "return None if user not exists" in new Context {
        awaitForResult(for {
          result <- authService.signIn(testUsername, testPassword)
        } yield result shouldBe None)
      }

    }

    "signUp" should {

      "return valid auth token" in new Context {
        awaitForResult(for {
          token <- authService.signUp(testUsername, testEmail, testPassword)
        } yield Jwt.decodeRaw(token, secretKey, Seq(JwtAlgorithm.HS256)).isSuccess shouldBe true)
      }

      "store auth data with encrypted password in database" in new Context {
        awaitForResult(for {
          _ <- authService.signUp(testUsername, testEmail, testPassword)
          Some(authData) <- authDataStorage.findAuthData(testUsername)
        } yield {
          authData.username shouldBe testUsername
          authData.email shouldBe testEmail
          authData.password shouldBe testPassword.sha256.hex
        })
      }

    }

  }

  trait Context {
    val secretKey = "secret"
    val authDataStorage = new InMemoryAuthDataStorage
    val authService = new AuthService(authDataStorage, secretKey)

    val testId: String = UUID.randomUUID().toString
    val testUsername: String = Random.nextString(10)
    val testEmail: String = Random.nextString(10)
    val testPassword: String = Random.nextString(10)

    val testAuthData = AuthData(testId, testUsername, testEmail, testPassword.sha256.hex)
  }

}
