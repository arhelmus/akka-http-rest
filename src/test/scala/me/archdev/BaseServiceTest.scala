package me.archdev

import akka.event.{LoggingAdapter, NoLogging}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import de.heikoseeberger.akkahttpcirce.CirceSupport
import org.scalatest._
import me.archdev.restapi.http.HttpService
import me.archdev.restapi.models.{TokenEntity, UserEntity}
import me.archdev.restapi.services.{AuthService, UsersService}
import me.archdev.restapi.utils.{DatabaseService, FlywayService}
import me.archdev.utils.InMemoryPostgresStorage._

import scala.concurrent.Await
import scala.concurrent.duration._

trait BaseServiceTest extends WordSpec with Matchers with ScalatestRouteTest with CirceSupport {

  val testUsers = Seq(
    UserEntity(Some(1), "Arhelmus", "test"),
    UserEntity(Some(2), "Arch", "test"),
    UserEntity(Some(3), "Hierarh", "test")
  )

  val testTokens = Seq(
    TokenEntity(userId = Some(1)),
    TokenEntity(userId = Some(2)),
    TokenEntity(userId = Some(3))
  )

  private val flywayService = new FlywayService(jdbcUrl, dbUser, dbPassword)
  private val databaseService = new DatabaseService(jdbcUrl, dbUser, dbPassword)

  val usersService = new UsersService(databaseService)
  val authService = new AuthService(databaseService)(usersService)
  val httpService = new HttpService(usersService, authService)

  dbProcess.getProcessId
  flywayService.dropDatabase.migrateDatabaseSchema
  provisionTestData

  private def provisionTestData = {
    import databaseService._
    import databaseService.driver.api._
    Await.result(db.run(usersService.users ++= testUsers), 10.seconds)
    Await.result(db.run(authService.tokens ++= testTokens), 10.seconds)
  }

}
