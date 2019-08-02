package com.example

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.example.Message.ReadBehaviorMessage.Read
import com.example.Message.WriteBehaviorMessage.Save
import com.example.Message.{ReadBehaviorMessage, WriteBehaviorMessage}
import com.example.ReadResponse.Data
import com.example.SaveResponse.Ok

object Actors {

  private def unhandled: Behaviors.Receive[Message[Response]] =
    Behaviors.receiveMessage[Message[Response]] { x =>
      x.replyTo ! Unhandled
      Behaviors.same
    }

  private def makeBehavior[T <: Message[_]](
    body: PartialFunction[T, Behavior[Message[Response]]]
  ): Behavior[Message[Response]] = {
    val handled: Behavior[Message[Response]] =
      Behaviors
        .receiveMessagePartial(body)
    handled orElse unhandled
  }

  def read(state: String): Behavior[Message[Response]] =
    makeBehavior[ReadBehaviorMessage] {
      case Read(replyTo) => replyTo ! Data(state); write(state)
    }

  def write(state: String): Behavior[Message[Response]] =
    makeBehavior[WriteBehaviorMessage] {
      case Save(replyTo, value) => replyTo ! Ok; read(value)
    }
}
