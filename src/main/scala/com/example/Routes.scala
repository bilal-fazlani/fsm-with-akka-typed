package com.example

import java.util.concurrent.TimeoutException

import akka.http.scaladsl.model.HttpEntity.Strict
import akka.http.scaladsl.model.{ContentTypes, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.{Directives, ExceptionHandler, Route}
import akka.util.ByteString

import scala.util.control.NonFatal

trait Routes extends Directives {
  val exceptionHandler: ExceptionHandler = ExceptionHandler({
    case _: TimeoutException =>
      complete(HttpResponse(StatusCodes.GatewayTimeout))
    case NonFatal(err) =>
      complete(
        HttpResponse(
          StatusCodes.InternalServerError,
          entity = Strict(
            ContentTypes.`text/plain(UTF-8)`,
            ByteString(
              (err.getMessage :: "" :: err.getStackTrace
                .map(x => x.toString)
                .toList)
                .mkString("\n")
            )
          )
        )
      )
  })

  def makeRoutes(proxy: ActorProxy): Route = {
    handleExceptions(exceptionHandler) {
      get {
        complete(proxy.read)
      } ~ post {
        parameter('data) { data =>
          complete(proxy.write(data))
        }
      }
    }
  }
}
