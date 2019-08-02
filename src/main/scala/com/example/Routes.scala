package com.example

import java.util.concurrent.TimeoutException

import akka.http.scaladsl.model.HttpEntity.Strict
import akka.http.scaladsl.model.{ ContentTypes, HttpResponse, StatusCodes }
import akka.http.scaladsl.server.{ Directives, ExceptionHandler, Route }
import akka.util.ByteString
import com.example.ReadResponse.Data
import com.example.SaveResponse.Ok

import scala.util.control.NonFatal
import scala.util.{ Failure, Success }

trait Routes extends Directives {
  private val exceptionHandler: ExceptionHandler = ExceptionHandler({
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
                .mkString("\n")))))
  })

  def makeRoutes(proxy: ActorProxy): Route = {
    handleExceptions(exceptionHandler) {
      get {
        onComplete(proxy.read) {
          case Success(Data(value)) => complete(value)
          case Success(Unhandled) =>
            complete(HttpResponse(StatusCodes.BadRequest))
          case Failure(exception) => throw exception
        }
      } ~
        (post & parameter('data)) { data =>
          onComplete(proxy.write(data)) {
            case Success(Ok) => complete("")
            case Success(Unhandled) =>
              complete(HttpResponse(StatusCodes.BadRequest))
            case Failure(exception) => throw exception
          }
        }
    }
  }
}
