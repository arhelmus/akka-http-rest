package me.archdev.restapi

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import me.archdev.restapi.http.HttpService
import me.archdev.restapi.services.{AuthService, UsersService}
import me.archdev.restapi.utils.{Config, DatabaseService, FlywayService}

import scala.concurrent.ExecutionContext

object Main extends App with Config {
  private implicit val system = ActorSystem()

  implicit val executor: ExecutionContext = system.dispatcher
  implicit val log: LoggingAdapter = Logging(system, getClass)
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val flywayService = new FlywayService(jdbcUrl, dbUser, dbPassword)
  flywayService.migrateDatabaseSchema

  val databaseService = new DatabaseService(jdbcUrl, dbUser, dbPassword)

  val usersService = new UsersService(databaseService)
  val authService = new AuthService(databaseService)(usersService)

  val httpService = new HttpService(usersService, authService)

  Http().bindAndHandle(httpService.routes, httpHost, httpPort)
}
