package com.example

import akka.actor.typed.ActorRef

sealed trait Message {
  def replyTo: ActorRef[Unhandled.type]
}

object Message {
  sealed trait WriteBehaviorMessage extends Message
  object WriteBehaviorMessage {
    case class Save(replyTo: ActorRef[WriteBehaviorResponse], value: String) extends WriteBehaviorMessage
    object Save {
      def apply(value: String)(replyTo: ActorRef[WriteBehaviorResponse]): Save = new Save(replyTo, value)
    }
    case class Clear(replyTo: ActorRef[WriteBehaviorResponse]) extends WriteBehaviorMessage
  }

  sealed trait ReadBehaviorMessage extends Message
  object ReadBehaviorMessage {
    case class Read(replyTo: ActorRef[ReadBehaviorResponse]) extends ReadBehaviorMessage
  }
}

sealed trait Response
sealed trait WriteBehaviorResponse extends Response
sealed trait ReadBehaviorResponse  extends Response
case object Unhandled              extends ReadBehaviorResponse with WriteBehaviorResponse

object WriteBehaviorResponse {
  case object Ok extends WriteBehaviorResponse
}

object ReadBehaviorResponse {
  case class Data(value: String) extends ReadBehaviorResponse
}
