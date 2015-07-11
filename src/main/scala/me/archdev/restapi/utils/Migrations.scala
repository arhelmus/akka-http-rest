package me.archdev.restapi.utils

import akka.event.LoggingAdapter
import me.archdev.restapi.models.db._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Success, Failure }

trait Migrations extends TokenEntityTable {

  import driver.api._

  protected val log: LoggingAdapter

  // TODO: implement correct migration mechanism with something like FlyWay or Liquibase
  def migrate(): Unit = {
    db.run((users.schema ++ tokens.schema).create)
  }
}
