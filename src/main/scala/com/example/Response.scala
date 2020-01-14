package com.example

sealed trait SaveResponse
sealed trait ClearResponse
sealed trait ReadResponse

case class Unhandled(state: String, messageType: String) extends SaveResponse with ClearResponse with ReadResponse {
  val description = s"Can not handle '$messageType' message in '$state' state"
}
case object Ok                 extends SaveResponse with ClearResponse
case class Data(value: String) extends ReadResponse
