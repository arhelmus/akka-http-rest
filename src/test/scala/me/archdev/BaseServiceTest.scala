package me.archdev

import akka.event.{LoggingAdapter, NoLogging}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest._

import me.archdev.restapi.http.HttpService
import me.archdev.restapi.models.{TokenEntity, UserEntity}
import me.archdev.restapi.utils.FlywayService
import me.archdev.utils.InMemoryPostgresStorage._

import scala.concurrent.Await
import scala.concurrent.duration._

trait BaseServiceTest extends WordSpec with Matchers with ScalatestRouteTest with HttpService {

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

  protected val log: LoggingAdapter = NoLogging
  private val flywayService = new FlywayService(jdbcUrl, dbUser, dbPassword)
  dbProcess.getProcessId
  flywayService.dropDatabase.migrateDatabaseSchema
  provisionTestData

  private def provisionTestData = {
    import driver.api._
    Await.result(db.run(users ++= testUsers), 10.seconds)
    Await.result(db.run(tokens ++= testTokens), 10.seconds)
  }

}
