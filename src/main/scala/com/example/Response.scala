package com.example

sealed trait Response
sealed trait SaveResponse  extends Response
sealed trait ClearResponse extends Response
sealed trait ReadResponse  extends Response

object Response {
  case object Unhandled          extends SaveResponse with ClearResponse with ReadResponse
  case object Ok                 extends SaveResponse with ClearResponse
  case class Data(value: String) extends ReadResponse
}
