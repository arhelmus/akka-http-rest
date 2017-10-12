package me.archdev.restapi.utils

import scala.concurrent.{ExecutionContext, Future, Promise}

/**
  * Monad transformers its classes that extends default monad's like Future or Option.
  * They handle situation when monad's contain each other, its helping to reduce amount of boilerplate code.
  * For example in that project there are lots of Future[Option[T]] classes that must be handled somehow.
  */
object MonadTransformers {

  implicit class FutureOptionMonadTransformer[A](t: Future[Option[A]])(implicit executionContext: ExecutionContext) {

    def mapT[B](f: A => B): Future[Option[B]] =
      t.map(_.map(f))

    def filterT(f: A => Boolean): Future[Option[A]] =
      t.map {
        case Some(data) if f(data) =>
          Some(data)
        case _ =>
          None
      }

    def flatMapT[B](f: A => Future[Option[B]]): Future[Option[B]] = {
      val resultPromise = Promise[Option[B]]

      t.flatMap {
        case Some(data) =>
          f(data).map(resultPromise.success)
        case None =>
          Future.successful()
      }.recover {
        case ex: Throwable =>
          resultPromise.failure(ex)
      }

      resultPromise.future
    }

    def flatMapTOuter[B](f: A => Future[B]): Future[Option[B]] =
      t.flatMap {
        case Some(data) =>
          f(data).map(Some.apply)
        case None =>
          Future.successful(None)
      }

    def flatMapTInner[B](f: A => Option[B]): Future[Option[B]] =
      t.map(_.flatMap(f))

  }

}
