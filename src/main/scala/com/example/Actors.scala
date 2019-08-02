package com.example

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.example.Message.AdminMessage.Save
import com.example.Message.ClientMessage.Read
import com.example.ReadResponse.Data
import com.example.SaveResponse.Ok
object Actors {
  def readonly(state: String): Behavior[Message] =
    Behaviors
      .receiveMessage[Message] {
        case Read(replyTo) =>
          replyTo ! Data(state); writeBeh(state)
        case x: Save => x.replyTo ! Unhandled; Behaviors.same
      }

  def writeBeh(state: String): Behavior[Message] =
    Behaviors
      .receiveMessagePartial[Message] {
        case Save(replyTo, data) =>
          replyTo ! Ok; readonly(data)
        case x: Read => x.replyTo ! Unhandled; Behaviors.same
      }
}
