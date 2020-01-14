package com.example

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object Main extends App with Directives with Routes {

  val port      = 8080
  val interface = "0.0.0.0"

  import Wiring._

  val ref = actorRef
  println("actor started")
  val proxy  = new ActorProxy(ref)
  val routes = makeRoutes(proxy)
  val bindingF = Http()
    .bindAndHandle(routes, interface, port)
    .map(binding => {
      println(s"http server started at $interface:$port")
      binding
    })

  bindingF.onComplete {
    case Failure(exception) =>
      exception.printStackTrace()
      system.terminate()
      sys.exit(1)
    case Success(_) =>
  }
}
