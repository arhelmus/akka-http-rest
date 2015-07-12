package me.archdev.restapi.http

import akka.http.scaladsl.server.Directives._
import me.archdev.restapi.http.routes._

trait HttpService extends UsersServiceRoute with AuthServiceRoute {

  val routes =
    pathPrefix("v1") {
      usersRoute ~
        authRoute
    }

}
