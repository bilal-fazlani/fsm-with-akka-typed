package com.example

import akka.http.scaladsl.server.{Directives, Route}

trait Routes extends Directives {
  def makeRoutes(proxy: ActorProxy): Route = {
    get {
      complete(proxy.read)
    } ~ post {
      parameter('data) { data =>
        complete(proxy.write(data))
      }
    }
  }
}
