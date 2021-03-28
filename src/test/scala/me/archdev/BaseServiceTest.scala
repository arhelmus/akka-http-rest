package me.archdev

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest._
import matchers.should._
import wordspec._
import org.mockito.MockitoSugar

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

trait BaseServiceTest extends AnyWordSpec with Matchers with ScalatestRouteTest with MockitoSugar {

  def awaitForResult[T](futureResult: Future[T]): T =
    Await.result(futureResult, 5.seconds)

}
