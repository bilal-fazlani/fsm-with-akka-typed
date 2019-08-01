package com.example

import akka.Done
import akka.actor.typed.ActorRef

sealed trait Message {
  val replyTo: ActorRef[_]
}

sealed trait AdminMessage extends Message

object AdminMessage {
  case class Save(value: String, replyTo: ActorRef[Done]) extends AdminMessage
}

sealed trait ClientMessage extends Message

object ClientMessage {
  case class Read(replyTo: ActorRef[String]) extends ClientMessage
}
