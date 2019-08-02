package com.example

import akka.actor
import akka.actor.Scheduler
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.adapter.TypedActorSystemOps
import akka.actor.typed.{ ActorRef, ActorSystem }
import akka.stream.ActorMaterializer
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration.DurationLong

object Wiring {
  implicit lazy val timeout: Timeout = Timeout(5.seconds)
  implicit lazy val system: ActorSystem[Message] =
    ActorSystem(Behaviors.empty, "system")
  implicit lazy val untypedSystem: actor.ActorSystem = system.toUntyped
  implicit lazy val mat: ActorMaterializer = ActorMaterializer()
  implicit lazy val sch: Scheduler = system.scheduler
  implicit lazy val actorRef: Future[ActorRef[Message]] =
    system.systemActorOf(Actors.write(""), "actor1")
}
