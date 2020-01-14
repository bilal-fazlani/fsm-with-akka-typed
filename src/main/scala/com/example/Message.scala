package com.example

import akka.actor.typed.ActorRef
import com.example.Response.Unhandled

sealed trait Message {
  def replyTo: ActorRef[Unhandled]
}

object Message {
  sealed trait WriteBehaviorMessage extends Message
  object WriteBehaviorMessage {
    case class Save(replyTo: ActorRef[SaveResponse], data: String) extends WriteBehaviorMessage
    object Save {
      def apply(value: String)(replyTo: ActorRef[SaveResponse]): Save = new Save(replyTo, value)
    }
    case class Clear(replyTo: ActorRef[ClearResponse]) extends WriteBehaviorMessage with ReadBehaviorMessage
  }

  sealed trait ReadBehaviorMessage extends Message
  object ReadBehaviorMessage {
    case class Read(replyTo: ActorRef[ReadResponse]) extends ReadBehaviorMessage
  }
}
