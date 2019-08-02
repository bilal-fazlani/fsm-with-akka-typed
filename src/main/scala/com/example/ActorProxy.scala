package com.example

import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.{ActorRef, Scheduler}
import akka.util.Timeout
import com.example.Message.AdminMessage.Save
import com.example.Message.ClientMessage.Read

import scala.concurrent.Future

class ActorProxy(actorRef: ActorRef[Message])(implicit timeout: Timeout,
                                              scheduler: Scheduler) {
  def read: Future[ReadResponse] = actorRef ? Read
  def write(data: String): Future[SaveResponse] = actorRef ? Save(data)
}
