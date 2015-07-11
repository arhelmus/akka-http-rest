package me.archdev.restapi

import akka.actor.ActorSystem
import akka.event.{ Logging, LoggingAdapter }
import akka.http.scaladsl.Http
import akka.stream.ActorFlowMaterializer

import me.archdev.restapi.http.HttpService
import me.archdev.restapi.utils.{ Migrations, Config }

import scala.concurrent.ExecutionContext

object Main extends App with Config with HttpService with Migrations {
  private implicit val system = ActorSystem()

  override protected implicit val executor: ExecutionContext = system.dispatcher
  override protected val log: LoggingAdapter = Logging(system, getClass)
  override protected implicit val materializer: ActorFlowMaterializer = ActorFlowMaterializer()

  migrate()

  Http().bindAndHandle(routes, httpInterface, httpPort)
}
