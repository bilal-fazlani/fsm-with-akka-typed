package com.example

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.example.Message.ReadBehaviorMessage.Read
import com.example.Message.WriteBehaviorMessage.{Clear, Save}
import com.example.Message.{ReadBehaviorMessage, WriteBehaviorMessage}
import com.example.Response.{Data, Ok, Unhandled}

import scala.reflect.ClassTag

object Actors {
  def read(state: String): Behavior[Message] = myReceive[ReadBehaviorMessage]("read") {
    case Read(replyTo) =>
      replyTo ! Data(state)
      write(state)
    case Clear(replyTo) =>
      replyTo ! Ok
      read("")
  }

  def write(state: String): Behavior[Message] = myReceive[WriteBehaviorMessage]("write") {
    case Save(replyTo, data) =>
      replyTo ! Ok
      read(data)
    case Clear(replyTo) =>
      replyTo ! Ok
      read("")
  }

  private def myReceive[B <: Message: ClassTag](stateName: String)(f: B => Behavior[Message]): Behavior[Message] =
    Behaviors.receiveMessage {
      case m: B => f(m)
      case m =>
        m.replyTo ! Unhandled(stateName, m.getClass.getSimpleName)
        Behaviors.same
    }
}
