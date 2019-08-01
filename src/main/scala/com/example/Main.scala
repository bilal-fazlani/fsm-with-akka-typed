package com.example

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives

import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App with Directives with Routes {

  val port = 8080
  val interface = "0.0.0.0"

  import Wiring._

  for {
    ref <- actorRef
    _ = println("actor started")
    proxy = new ActorProxy(ref)
    routes = makeRoutes(proxy)
    _ <- Http().bindAndHandle(routes, interface, port)
    _ = println(s"http server started at $interface:$port")
  } yield ()
}
