package me.archdev.core.profiles

import java.util.UUID

import me.archdev.BaseServiceTest
import me.archdev.restapi.core.{UserProfile, UserProfileUpdate}
import me.archdev.restapi.core.profiles.{InMemoryUserProfileStorage, UserProfileService}

import scala.util.Random

class UserProfileServiceTest extends BaseServiceTest {

  "UserProfileService" when {

    "getProfiles" should {

      "return all stored profiles" in new Context {
        awaitForResult(for {
          _ <- userProfileStorage.saveProfile(testProfile1)
          _ <- userProfileStorage.saveProfile(testProfile2)
          profiles <- userProfileService.getProfiles()
        } yield profiles shouldBe Seq(testProfile1, testProfile2))
      }

    }

    "getProfile" should {

      "return profile by id" in new Context {
        awaitForResult(for {
          _ <- userProfileStorage.saveProfile(testProfile1)
          _ <- userProfileStorage.saveProfile(testProfile2)
          maybeProfile <- userProfileService.getProfile(testProfileId1)
        } yield maybeProfile shouldBe Some(testProfile1))
      }

      "return None if profile not exists" in new Context {
        awaitForResult(for {
          _ <- userProfileStorage.saveProfile(testProfile1)
          _ <- userProfileStorage.saveProfile(testProfile2)
          maybeProfile <- userProfileService.getProfile("wrongId")
        } yield maybeProfile shouldBe None)
      }

    }

    "createProfile" should {

      "store profile" in new Context {
        awaitForResult(for {
          _ <- userProfileService.createProfile(testProfile1)
          maybeProfile <- userProfileStorage.getProfile(testProfileId1)
        } yield maybeProfile shouldBe Some(testProfile1))
      }

    }

    "updateProfile" should {

      "merge profile with partial update" in new Context {
        awaitForResult(for {
          _ <- userProfileService.createProfile(testProfile1)
          _ <- userProfileService.updateProfile(testProfileId1, UserProfileUpdate(Some("test"), Some("test")))
          maybeProfile <- userProfileStorage.getProfile(testProfileId1)
        } yield maybeProfile shouldBe Some(testProfile1.copy(firstName = "test", lastName = "test")))
      }

      "return None if profile is not exists" in new Context {
        awaitForResult(for {
          maybeProfile <- userProfileService.updateProfile(testProfileId1, UserProfileUpdate(Some("test"), Some("test")))
        } yield maybeProfile shouldBe None)
      }

    }

  }

  trait Context {
    val userProfileStorage = new InMemoryUserProfileStorage()
    val userProfileService = new UserProfileService(userProfileStorage)

    val testProfileId1: String = UUID.randomUUID().toString
    val testProfileId2: String = UUID.randomUUID().toString
    val testProfile1: UserProfile = testProfile(testProfileId1)
    val testProfile2: UserProfile = testProfile(testProfileId2)

    def testProfile(id: String) = UserProfile(id, Random.nextString(10), Random.nextString(10))
  }

}
