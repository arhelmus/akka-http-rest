package me.archdev.core.profiles

import java.util.UUID

import me.archdev.BaseServiceTest
import me.archdev.restapi.core.UserProfile
import me.archdev.restapi.core.profiles.{InMemoryUserProfileStorage, JdbcUserProfileStorage, UserProfileStorage}
import me.archdev.utils.InMemoryPostgresStorage

import scala.util.Random

class JdbcUserProfileStorageTest extends UserProfileStorageSpec {
  override def userProfileStorageBuilder(): UserProfileStorage =
    new JdbcUserProfileStorage(InMemoryPostgresStorage.databaseConnector)
}

class InMemoryUserProfileStorageTest extends UserProfileStorageSpec {
  override def userProfileStorageBuilder(): UserProfileStorage =
    new InMemoryUserProfileStorage()
}

abstract class UserProfileStorageSpec extends BaseServiceTest {

  def userProfileStorageBuilder(): UserProfileStorage

  "UserProfileStorage" when {

    "getProfile" should {

      "return profile by id" in new Context {
        awaitForResult(for {
          _ <- userProfileStorage.saveProfile(testProfile1)
          _ <- userProfileStorage.saveProfile(testProfile2)
          maybeProfile <- userProfileStorage.getProfile(testProfileId1)
        } yield maybeProfile shouldBe Some(testProfile1))
      }

      "return None if profile not found" in new Context {
        awaitForResult(for {
          maybeProfile <- userProfileStorage.getProfile("smth")
        } yield maybeProfile shouldBe None)
      }

    }

    "getProfiles" should {

      "return all profiles from database" in new Context {
        awaitForResult(for {
          _ <- userProfileStorage.saveProfile(testProfile1)
          _ <- userProfileStorage.saveProfile(testProfile2)
          profiles <- userProfileStorage.getProfiles()
        } yield profiles.nonEmpty shouldBe true)
      }

    }

    "saveProfile" should {

      "save profile to database" in new Context {
        awaitForResult(for {
          _ <- userProfileStorage.saveProfile(testProfile1)
          maybeProfile <- userProfileStorage.getProfile(testProfileId1)
        } yield maybeProfile shouldBe Some(testProfile1))
      }

      "overwrite profile if it exists" in new Context {
        awaitForResult(for {
          _ <- userProfileStorage.saveProfile(testProfile1.copy(firstName = "test", lastName = "test"))
          _ <- userProfileStorage.saveProfile(testProfile1)
          maybeProfile <- userProfileStorage.getProfile(testProfileId1)
        } yield maybeProfile shouldBe Some(testProfile1))
      }

    }

  }

  trait Context {
    val userProfileStorage: UserProfileStorage = userProfileStorageBuilder()
    val testProfileId1: String = UUID.randomUUID().toString
    val testProfileId2: String = UUID.randomUUID().toString
    val testProfile1: UserProfile = testProfile(testProfileId1)
    val testProfile2: UserProfile = testProfile(testProfileId2)

    def testProfile(id: String) = UserProfile(id, Random.nextString(10), Random.nextString(10))
  }

}
