package com.example

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

object Main extends App with Directives with Routes {

  val port      = 8080
  val interface = "0.0.0.0"

  import Wiring._

  val f = for {
    ref    <- actorRef
    _      = println("actor started")
    proxy  = new ActorProxy(ref)
    routes = makeRoutes(proxy)
    _      <- Http().bindAndHandle(routes, interface, port)
    _      = println(s"http server started at $interface:$port")
  } yield ()

  f.onComplete {
    case Failure(exception) =>
      exception.printStackTrace()
      system.terminate()
      sys.exit(1)
    case Success(_) =>
  }
}
