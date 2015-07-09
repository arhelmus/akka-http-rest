package me.archdev.restapi.http.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers.IntNumber
import me.archdev.restapi.http.BaseService
import me.archdev.restapi.models.{ UserEntityUpdate, UserEntity }
import me.archdev.restapi.services.UsersService

import spray.json._

trait UsersServiceRoute extends BaseService with UsersService {

  import StatusCodes._

  implicit val usersUpdateFormat = jsonFormat2(UserEntityUpdate)

  val usersRoute = pathPrefix("users") {
    pathEndOrSingleSlash {
      get {
        complete(getUsers().toJson)
      } ~
        post {
          entity(as[UserEntity]) { userEntity =>
            complete(Created -> createUser(userEntity).toJson)
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
            post {
              entity(as[UserEntityUpdate]) { userUpdate =>
                complete(updateUser(id, userUpdate).toJson)
              }
            } ~
            delete {
              complete(NoContent -> deleteUser(id).toJson)
            }
        }
      }
  }

}
