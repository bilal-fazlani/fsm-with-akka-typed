package com.example

sealed trait Response
sealed trait WriteBehaviorResponse extends Response
sealed trait ReadBehaviorResponse  extends Response

object Response {
  case object Unhandled          extends WriteBehaviorResponse with ReadBehaviorResponse
  case object Ok                 extends WriteBehaviorResponse with ReadBehaviorResponse
  case class Data(value: String) extends ReadBehaviorResponse
}
