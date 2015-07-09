package ru.gamelax

import akka.event.{ NoLogging, LoggingAdapter }
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{ WordSpec, Matchers }

trait ServiceTestBase extends WordSpec with Matchers with ScalatestRouteTest {
  protected def log: LoggingAdapter = NoLogging
}
