package com.example

import akka.actor.Scheduler
import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.util.Timeout
import com.example.Message.ReadBehaviorMessage.Read
import com.example.Message.WriteBehaviorMessage.{Clear, Save}

import scala.concurrent.Future

class ActorProxy(actorRef: ActorRef[Message])(implicit timeout: Timeout, scheduler: Scheduler) {
  def read: Future[ReadResponse]               = actorRef ? Read
  def clear: Future[ClearResponse]             = actorRef ? Clear
  def save(data: String): Future[SaveResponse] = actorRef ? Save(data)
}
