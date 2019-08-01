package com.example

import akka.Done
import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior, Scheduler}
import akka.util.Timeout
import com.example.AdminMessage.Save
import com.example.ClientMessage.Read

import scala.concurrent.Future
import scala.concurrent.duration.DurationLong
object Actors {

  def readonly(state: String): Behavior[Message] =
    Behaviors
      .receiveMessagePartial[Message] {
        case ClientMessage.Read(replyTo) =>
          replyTo ! state; writeBeh(state)
      }

  def writeBeh(state: String): Behavior[Message] =
    Behaviors.receiveMessagePartial[Message] {
      case Save(value, replyTo) =>
        replyTo ! Done; readonly(value)
    }

  def start(state: String): Future[ActorRef[Message]] = {
    implicit val timeout: Timeout = Timeout(5.seconds)
    val sys: ActorSystem[Message] =
      ActorSystem(Behaviors.empty, "system")
    sys.systemActorOf(writeBeh(""), "actor1")
  }
}

class ActorProxy(actorRef: ActorRef[Message])(implicit timeout: Timeout,
                                              scheduler: Scheduler) {
  def read: Future[String] = actorRef ? Read
  def write(data: String): Future[Done] = actorRef.ask[Done](Save(data, _))
}
