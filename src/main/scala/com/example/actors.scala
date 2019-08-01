package com.example

import akka.Done
import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior, Scheduler}
import akka.util.Timeout
import com.example.AdminMessage.Save
import com.example.ClientMessage.Read

import scala.concurrent.Future
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
}

class ActorProxy(actorRef: ActorRef[Message])(implicit timeout: Timeout,
                                              scheduler: Scheduler) {
  def read: Future[String] = actorRef ? Read
  def write(data: String): Future[Done] = actorRef.ask[Done](Save(data, _))
}
