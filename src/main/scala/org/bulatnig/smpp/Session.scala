package org.bulatnig.smpp

import java.net.InetSocketAddress

import akka.actor.{Props, ActorRef, Actor}
import org.bulatnig.smpp.net.Connection
import org.bulatnig.smpp.pdu.BindTransceiver

object Session {
  def props(remote: InetSocketAddress) = Props(new Session(remote))
}

class Session(remote: InetSocketAddress) extends Actor {

  var connection: ActorRef = null

  override def preStart(): Unit = {
    connection = context.actorOf(Connection.props(remote, self))
    context.watch(connection)
  }

  def receive = {
    case "connected" =>
      connection ! new BindTransceiver(sequenceNumber = 1, systemId = "smppclient1", password = "password")
    case _ =>
  }

}
