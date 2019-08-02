package com.example

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import com.example.Message.ReadBehaviorMessage.Read
import com.example.Message.WriteBehaviorMessage.{Clear, Save}
import com.example.ReadResponse.Data
import com.example.SaveResponse.Ok

object Actors {

  val unhandled: Behavior[Message[Response]] =
    Behaviors.receiveMessage[Message[Response]] { x =>
      // this cast is ok, given the domain knowledge that only _ <: Response can only be Unhandled if it reaches here
      x.replyTo.asInstanceOf[ActorRef[Unhandled.type]] ! Unhandled
      Behaviors.same
    }

  def read(state: String): Behavior[Message[Response]] = {
    Behaviors.receiveMessagePartial[Message[Response]] {
      case Read(replyTo) =>
        replyTo ! Data(state)
        write(state)
    }
  }.orElse(unhandled)

  def write(state: String): Behavior[Message[Response]] = {
    Behaviors.receiveMessagePartial[Message[Response]] {
      case Save(replyTo, value) =>
        replyTo ! Ok
        read(value)

      case Clear(replyTo) =>
        replyTo ! Ok
        read("")
    }
  }.orElse(unhandled)
}
