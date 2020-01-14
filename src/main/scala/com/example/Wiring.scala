package com.example

import akka.actor
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.adapter._
import akka.actor.typed.{ActorRef, ActorSystem, Scheduler}
import akka.stream.Materializer
import akka.util.Timeout

import scala.concurrent.duration.DurationLong

object Wiring {
  implicit lazy val timeout: Timeout                 = Timeout(5.seconds)
  implicit lazy val system: ActorSystem[_]           = ActorSystem(Behaviors.empty, "system")
  implicit lazy val untypedSystem: actor.ActorSystem = system.toClassic
  implicit lazy val mat: Materializer                = Materializer(system)
  implicit lazy val sch: Scheduler                   = system.scheduler
  implicit lazy val actorRef: ActorRef[Message]      = system.systemActorOf(Actors.write(""), "actor1")
}
