package com.example

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.example.Message.ReadBehaviorMessage.Read
import com.example.Message.WriteBehaviorMessage.{Clear, Save}
import com.example.Message.{ReadBehaviorMessage, WriteBehaviorMessage}
import com.example.ReadResponse.Data
import com.example.SaveResponse.Ok

object Actors {

  val unhandled: Behavior[Message[Response]] =
    Behaviors.receiveMessage[Message[Response]] { x =>
      x.replyTo ! Unhandled
      Behaviors.same
    }

  def read(state: String): Behavior[Message[Response]] = {
    Behaviors.receiveMessage[ReadBehaviorMessage] {
      case Read(replyTo) =>
        replyTo ! Data(state)
        write(state)
    }
  }.orElse(unhandled)

  def write(state: String): Behavior[Message[Response]] = {
    Behaviors.receiveMessage[WriteBehaviorMessage] {
      case Save(replyTo, value) =>
        replyTo ! Ok
        read(value)

      case Clear(replyTo) =>
        replyTo ! Ok
        read("")
    }
  }.orElse(unhandled)
}
