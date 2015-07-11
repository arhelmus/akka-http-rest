package me.archdev.restapi.utils

import akka.event.LoggingAdapter
import me.archdev.restapi.models.db._
import slick.jdbc.meta.MTable

import scala.concurrent.Future

trait Migrations extends TokenEntityTable {

  import driver.api._

  protected val log: LoggingAdapter

  // TODO: implement correct migration mechanism with something like FlyWay or Liquibase
  def migrate(): Future[Unit] = {
    createSchema()
  }

  def createSchema(): Future[Unit] = {
    db.run((users.schema ++ tokens.schema).create)
  }

  def dropSchema(): Future[Unit] = {
    db.run((users.schema ++ tokens.schema).drop)
  }
}
