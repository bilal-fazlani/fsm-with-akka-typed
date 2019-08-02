package com.example

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.example.Message.ReadBehaviorMessage.Read
import com.example.Message.WriteBehaviorMessage.{ Clear, Save }
import com.example.ReadResponse.Data
import com.example.SaveResponse.Ok

object Actors {
  def read(state: String): Behavior[Message] = {
    Behaviors.receiveMessage[Message] {
      case Read(replyTo) =>
        replyTo ! Data(state)
        write(state)
      case m =>
        m.replyTo ! Unhandled
        Behaviors.same
    }
  }

  def write(state: String): Behavior[Message] = {
    Behaviors.receiveMessage[Message] {
      case Save(replyTo, value) =>
        replyTo ! Ok
        read(value)
      case Clear(replyTo) =>
        replyTo ! Ok
        read("")
      case m =>
        m.replyTo ! Unhandled
        Behaviors.same
    }
  }
}
