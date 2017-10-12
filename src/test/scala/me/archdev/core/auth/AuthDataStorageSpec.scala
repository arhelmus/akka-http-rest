package me.archdev.core.auth

import java.util.UUID

import me.archdev.BaseServiceTest
import me.archdev.restapi.core.AuthData
import me.archdev.restapi.core.auth.{AuthDataStorage, InMemoryAuthDataStorage, JdbcAuthDataStorage}
import me.archdev.utils.InMemoryPostgresStorage

import scala.util.Random

class JdbcAuthDataStorageTest extends AuthDataStorageSpec {
  override def authDataStorageBuilder(): AuthDataStorage =
    new JdbcAuthDataStorage(InMemoryPostgresStorage.databaseConnector)
}

class InMemoryAuthDataStorageTest extends AuthDataStorageSpec {
  override def authDataStorageBuilder(): AuthDataStorage =
    new InMemoryAuthDataStorage()
}

abstract class AuthDataStorageSpec extends BaseServiceTest {

  def authDataStorageBuilder(): AuthDataStorage

  "AuthDataStorage" when {

    "saveAuthData" should {

      "return saved auth data" in new Context {
        awaitForResult(for {
          authData <- authDataStorage.saveAuthData(testAuthData)
        } yield authData shouldBe testAuthData)
      }

      "override already saved data" in new Context {
        awaitForResult(for {
          _ <- authDataStorage.saveAuthData(testAuthData.copy(username = "123", email = "123", password = "123"))
          authData <- authDataStorage.saveAuthData(testAuthData)
        } yield authData shouldBe testAuthData)
      }

    }

    "findAuthData" should {

      "return auth data where username or email equals to login" in new Context {
        awaitForResult(for {
          _ <- authDataStorage.saveAuthData(testAuthData)
          maybeAuthDataUsername <- authDataStorage.findAuthData(testAuthData.username)
          maybeAuthDataEmail <- authDataStorage.findAuthData(testAuthData.email)
        } yield {
          maybeAuthDataUsername shouldBe Some(testAuthData)
          maybeAuthDataEmail shouldBe Some(testAuthData)
        })
      }

      "return None if user with such login don't exists" in new Context {
        awaitForResult(for {
          maybeAuthData <- authDataStorage.findAuthData(testAuthData.username)
        } yield maybeAuthData shouldBe None)
      }

    }

  }

  trait Context {
    val authDataStorage: AuthDataStorage = authDataStorageBuilder()
    val testAuthData = AuthData(UUID.randomUUID().toString, Random.nextString(10), Random.nextString(10), Random.nextString(10))
  }

}
