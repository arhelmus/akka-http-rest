package me.archdev.restapi.http.routes

import akka.event.LoggingAdapter
import akka.stream.ActorMaterializer
import de.heikoseeberger.akkahttpcirce.CirceSupport
import me.archdev.restapi.utils.Config

import scala.concurrent.ExecutionContext

trait BaseServiceRoute extends CirceSupport with Config {
  protected implicit def executor: ExecutionContext
  protected implicit def materializer: ActorMaterializer
  protected def log: LoggingAdapter
}
