package com.example

import akka.actor
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.adapter.TypedActorSystemOps
import akka.actor.typed.{ActorSystem, Scheduler}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.example.Actors.writeBeh

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationLong

object Main extends App with Directives {

  val port = 8080
  val interface = "0.0.0.0"

  implicit val timeout: Timeout = Timeout(5.seconds)
  implicit val system: ActorSystem[Message] =
    ActorSystem(Behaviors.empty, "system")
  implicit val untypedSystem: actor.ActorSystem = system.toUntyped
  system.systemActorOf(writeBeh(""), "actor1")
  implicit val mat: ActorMaterializer = ActorMaterializer()
  implicit val sch: Scheduler = system.scheduler

  for {
    ref <- Actors.start("")
    _ = println("actor started")
    proxy = new ActorProxy(ref)
    routes = makeRoutes(proxy)
    _ <- Http().bindAndHandle(routes, interface, port)
    _ = println(s"http server started at $interface:$port")
  } yield ()

  def makeRoutes(proxy: ActorProxy): Route =
    get {
      complete(proxy.read)
    } ~ post {
      parameter('data) { data =>
        complete(proxy.write(data))
      }
    }
}
