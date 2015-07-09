package me.archdev.restapi.http.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers.IntNumber
import me.archdev.restapi.http.BaseService
import me.archdev.restapi.models.UserEntity
import me.archdev.restapi.services.UsersService

import spray.json._

trait UsersServiceRoute extends BaseService with UsersService {

  import StatusCodes._

  val usersRoute = pathPrefix("users") {
    pathEndOrSingleSlash {
      get {
        complete(getUsers().toJson)
      } ~
        post {
          entity(as[UserEntity]) { userEntity =>
            complete(Created -> saveUser(userEntity).toJson)
          }
        }
    } ~
      pathPrefix("me") {
        pathEndOrSingleSlash {
          get {
            complete("get current user")
          } ~
            put {
              complete("update current user")
            }
        }
      } ~
      pathPrefix(IntNumber) { id =>
        pathEndOrSingleSlash {
          get {
            complete(getUserById(id).toJson)
          } ~
            put {
              complete("update user " + id)
            } ~
            delete {
              complete(NoContent -> deleteUser(id).toJson)
            }
        }
      }
  }

}
