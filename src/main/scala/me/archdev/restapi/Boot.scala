package me.archdev.restapi

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import me.archdev.restapi.core.auth.{AuthService, JdbcAuthDataStorage}
import me.archdev.restapi.core.profiles.{JdbcUserProfileStorage, UserProfileService}
import me.archdev.restapi.http.HttpRoute
import me.archdev.restapi.utils.Config
import me.archdev.restapi.utils.db.{DatabaseConnector, DatabaseMigrationManager}

import scala.concurrent.ExecutionContext

object Boot extends App {

  def startApplication() = {
    implicit val actorSystem = ActorSystem()
    implicit val executor: ExecutionContext = actorSystem.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    val config = Config.load()

    new DatabaseMigrationManager(
      config.database.jdbcUrl,
      config.database.username,
      config.database.password
    ).migrateDatabaseSchema()

    val databaseConnector = new DatabaseConnector(
      config.database.jdbcUrl,
      config.database.username,
      config.database.password
    )

    val userProfileStorage = new JdbcUserProfileStorage(databaseConnector)
    val authDataStorage = new JdbcAuthDataStorage(databaseConnector)

    val usersService = new UserProfileService(userProfileStorage)
    val authService = new AuthService(authDataStorage, config.secretKey)
    val httpRoute = new HttpRoute(usersService, authService, config.secretKey)

    Http().bindAndHandle(httpRoute.route, config.http.host, config.http.port)
  }

  startApplication()

}
