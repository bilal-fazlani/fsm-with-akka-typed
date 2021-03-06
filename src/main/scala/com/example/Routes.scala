package com.example

import java.util.concurrent.TimeoutException

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, ExceptionHandler, Route}

import scala.util.control.NonFatal

trait Routes extends Directives {
  private val exceptionHandler: ExceptionHandler = ExceptionHandler {
    case _: TimeoutException => complete(StatusCodes.GatewayTimeout)
    case NonFatal(err)       => complete(StatusCodes.InternalServerError -> err.getStackTrace.mkString("\n"))
  }

  def makeRoutes(proxy: ActorProxy): Route = handleExceptions(exceptionHandler) {
    get {
      onSuccess(proxy.read) {
        case Data(value)  => complete(value)
        case x: Unhandled => complete(StatusCodes.BadRequest -> x.description)
      }
    } ~
    delete {
      onSuccess(proxy.clear) {
        case Ok           => complete("")
        case x: Unhandled => complete(StatusCodes.BadRequest -> x.description)
      }
    } ~
    (post & parameter("data")) { data =>
      onSuccess(proxy.save(data)) {
        case Ok           => complete("")
        case x: Unhandled => complete(StatusCodes.BadRequest -> x.description)
      }
    }
  }
}
