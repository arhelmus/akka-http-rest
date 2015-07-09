package me.archdev.restapi.models

import java.util.UUID

case class TokenEntity(id: Option[Long], token: String = UUID.randomUUID().toString.replaceAll("-", ""))
