package org.bulatnig.smpp.net

import java.net.InetSocketAddress

import akka.actor.{Terminated, ActorRef, ActorSystem}
import org.bulatnig.smpp.Session
import org.scalatest.FlatSpec

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class ConnectionTest extends FlatSpec {

  "A connection" should "connect" in {
    val system = ActorSystem("mySystem")
    val connection = system.actorOf(Session.props(new InetSocketAddress("localhost", 2775)))
    Thread.sleep(10 * 1000)
    val termFuture = system.terminate()
    Await.result(termFuture, Duration.Inf)
  }

}
