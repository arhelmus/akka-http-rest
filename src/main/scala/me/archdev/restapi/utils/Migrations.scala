package me.archdev.restapi.utils

import akka.event.LoggingAdapter
import me.archdev.restapi.models.db._

trait Migrations extends TokenEntityTable {

  import driver.api._

  protected val log: LoggingAdapter

  // TODO: implement correct migration mechanism with something like FlyWay or Liquibase
  def migrate(): Unit = {
    db.run((users.schema ++ tokens.schema).create)
  }
}
