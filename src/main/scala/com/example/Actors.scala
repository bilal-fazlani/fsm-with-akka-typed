package com.example

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.example.Message.{ ReadBehaviorMessage, WriteBehaviorMessage }
import com.example.Message.ReadBehaviorMessage.Read
import com.example.Message.WriteBehaviorMessage.{ Clear, Save }
import com.example.ReadResponse.Data
import com.example.SaveResponse.Ok

import scala.reflect.ClassTag

object Actors {
  def read(state: String): Behavior[Message] = myReceive[ReadBehaviorMessage] {
    case Read(replyTo) =>
      replyTo ! Data(state)
      write(state)
  }

  def write(state: String): Behavior[Message] = myReceive[WriteBehaviorMessage] {
    case Save(replyTo, value) =>
      replyTo ! Ok
      read(value)
    case Clear(replyTo) =>
      replyTo ! Ok
      read("")
  }

  def unhandled(m: Message): Behavior[Message] = {
    m.replyTo ! Unhandled
    Behaviors.same
  }

  private def myReceive[B <: Message: ClassTag](f: B => Behavior[Message]): Behavior[Message] = Behaviors.receiveMessage {
    case x: B => f(x)
    case x => unhandled(x)
  }
}
