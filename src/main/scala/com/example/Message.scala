package com.example

import akka.Done
import akka.actor.typed.ActorRef

sealed trait Message {
  val replyTo: ActorRef[_]
}

object Message {
  sealed trait AdminMessage extends Message

  object AdminMessage {
    case class Save(replyTo: ActorRef[Done], value: String) extends AdminMessage
    object Save {
      def apply(value: String)(replyTo: ActorRef[Done]): Save =
        new Save(replyTo, value)
    }
  }

  sealed trait ClientMessage extends Message

  object ClientMessage {
    case class Read(replyTo: ActorRef[String]) extends ClientMessage
  }

}
