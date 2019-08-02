package com.example

import akka.actor.typed.ActorRef

sealed trait Message[T <: Response] {
  val replyTo: ActorRef[T]
}

object Message {
  sealed trait WriteBehaviorMessage extends Message[SaveResponse]

  object WriteBehaviorMessage {
    case class Save(replyTo: ActorRef[SaveResponse], value: String)
        extends WriteBehaviorMessage

    case class Clear(replyTo: ActorRef[SaveResponse])
        extends WriteBehaviorMessage
  }

  sealed trait ReadBehaviorMessage extends Message[ReadResponse]

  object ReadBehaviorMessage {
    case class Read(replyTo: ActorRef[ReadResponse]) extends ReadBehaviorMessage
  }

}

sealed trait Response
sealed trait SaveResponse extends Response
sealed trait ReadResponse extends Response
case object Unhandled extends ReadResponse with SaveResponse

object SaveResponse {
  case object Ok extends SaveResponse
}

object ReadResponse {
  case class Data(value: String) extends ReadResponse
}
