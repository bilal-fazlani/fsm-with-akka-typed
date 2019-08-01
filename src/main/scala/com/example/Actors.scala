package com.example

import akka.Done
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.example.Message.AdminMessage.Save
import com.example.Message.ClientMessage
object Actors {
  def readonly(state: String): Behavior[Message] =
    Behaviors
      .receiveMessagePartial[Message] {
        case ClientMessage.Read(replyTo) =>
          replyTo ! state; writeBeh(state)
      }

  def writeBeh(state: String): Behavior[Message] =
    Behaviors.receiveMessagePartial[Message] {
      case Save(replyTo, data) =>
        replyTo ! Done; readonly(data)
    }
}
