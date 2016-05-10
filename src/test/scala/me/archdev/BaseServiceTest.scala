package me.archdev

import akka.http.scaladsl.testkit.ScalatestRouteTest
import de.heikoseeberger.akkahttpcirce.CirceSupport
import me.archdev.restapi.http.HttpService
import me.archdev.restapi.models.{TokenEntity, UserEntity}
import me.archdev.restapi.services.{AuthService, UsersService}
import me.archdev.restapi.utils.{DatabaseService, FlywayService}
import me.archdev.utils.InMemoryPostgresStorage._
import org.scalatest._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.Random

trait BaseServiceTest extends WordSpec with Matchers with ScalatestRouteTest with CirceSupport {

  private val flywayService = new FlywayService(jdbcUrl, dbUser, dbPassword)
  private val databaseService = new DatabaseService(jdbcUrl, dbUser, dbPassword)

  val usersService = new UsersService(databaseService)
  val authService = new AuthService(databaseService)(usersService)
  val httpService = new HttpService(usersService, authService)

  def provisionUsersList(size: Int): Seq[UserEntity] = {
    val savedUsers = (1 to size).map { _ =>
      UserEntity(Some(Random.nextLong()), Random.nextString(10), Random.nextString(10))
    }.map(usersService.createUser)

    Await.result(Future.sequence(savedUsers), 10.seconds)
  }

  def provisionTokensForUsers(usersList: Seq[UserEntity]) = {
    val savedTokens = usersList.map(authService.createToken)
    Await.result(Future.sequence(savedTokens), 10.seconds)
  }

  dbProcess.getProcessId
  flywayService.dropDatabase.migrateDatabaseSchema

}
