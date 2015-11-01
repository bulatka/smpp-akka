package org.bulatnig.smpp.net

import java.util.Base64

import akka.actor.{ActorLogging, Actor, ActorRef, Props}
import akka.io.{IO, Tcp}
import akka.util.ByteString
import java.net.InetSocketAddress

import org.bulatnig.smpp.Buffer
import org.bulatnig.smpp.pdu.{PDU, BindTransceiver}

/**
 *
 * @param remote
 * @param listener
 */
class Connection(remote: InetSocketAddress, listener: ActorRef) extends Actor with ActorLogging {

  import Tcp._
  import context.system

  IO(Tcp) ! Connect(remote)

  def receive = {
    case CommandFailed(_: Connect) =>
      log.info("connect failed")
      context stop self

    case c@Connected(remote, local) =>
      log.info("connected")
      // listener ! c
      val connection = sender()
      connection ! Register(self)
      context become {
        case pdu: PDU =>
          val write: Write = Write(ByteString(pdu.toBuffer.toArray), NoAck)
          write.wantsAck
          connection ! write
        case CommandFailed(w: Write) =>
          // O/S buffer was full
          log.info("write failed")
        case Received(data) =>
          log.info(new Buffer(data.toArray).toHexString)
        case "close" =>
          connection ! Close
        case _: ConnectionClosed =>
          log.info("connection closed")
          context stop self
      }
      listener ! "connected"
  }
}

object Connection {
  def props(remote: InetSocketAddress, listener: ActorRef) = Props(new Connection(remote, listener))
}