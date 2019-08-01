package com.example

import akka.Done
import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.{ActorRef, Scheduler}
import akka.util.Timeout
import com.example.Message.AdminMessage.Save
import com.example.Message.ClientMessage.Read

import scala.concurrent.Future

class ActorProxy(actorRef: ActorRef[Message])(implicit timeout: Timeout,
                                              scheduler: Scheduler) {
  def read: Future[String] = actorRef ? Read
  def write(data: String): Future[Done] = actorRef.ask[Done](Save(data, _))
}
