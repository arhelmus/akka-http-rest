package me.archdev

import me.archdev.restapi.models.{ TokenEntity, UserEntity }
import me.archdev.restapi.utils.Migrations
import org.scalatest._

import akka.event.{ NoLogging, LoggingAdapter }
import akka.http.scaladsl.testkit.ScalatestRouteTest

import scala.concurrent.Await
import scala.concurrent.duration._

trait BaseServiceTest extends WordSpec with Matchers with ScalatestRouteTest with Migrations {
  protected val log: LoggingAdapter = NoLogging

  import driver.api._

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

  try {
    Await.result(dropSchema(), 10.seconds)
  } catch {
    case _: Throwable =>
  }
  Await.result(createSchema(), 10.seconds)
  Await.result(db.run(users ++= testUsers), 10.seconds)
  Await.result(db.run(tokens ++= testTokens), 10.seconds)
}
