package me.archdev.restapi.utils

import me.archdev.restapi.models.UserEntity
import spray.json.DefaultJsonProtocol

trait Protocol extends DefaultJsonProtocol {
  implicit val usersFormat = jsonFormat2(UserEntity)
}
