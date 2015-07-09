package me.archdev.restapi.http

import akka.http.scaladsl.server.Directives._
import me.archdev.restapi.http.routes.UsersServiceRoute

trait HttpService extends BaseService with UsersServiceRoute {

  val routes =
    pathPrefix("v1") {
      usersRoute
    }

}
