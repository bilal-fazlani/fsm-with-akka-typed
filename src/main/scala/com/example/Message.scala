package com.example

import akka.actor.typed.ActorRef

sealed trait Message {
  val replyTo: ActorRef[_]
}

object Message {
  sealed trait AdminMessage extends Message

  object AdminMessage {
    case class Save(replyTo: ActorRef[SaveResponse], value: String)
        extends AdminMessage
    object Save {
      def apply(value: String)(replyTo: ActorRef[SaveResponse]): Save =
        new Save(replyTo, value)
    }
  }

  sealed trait ClientMessage extends Message

  object ClientMessage {
    case class Read(replyTo: ActorRef[ReadResponse]) extends ClientMessage
  }

}

sealed trait SaveResponse
sealed trait ReadResponse
case object Unhandled extends SaveResponse with ReadResponse

object SaveResponse {
  case object Ok extends SaveResponse
}

object ReadResponse {
  case class Data(value: String) extends ReadResponse
}
