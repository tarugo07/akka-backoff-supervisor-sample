package com.example

import akka.actor.Actor.Receive
import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, PoisonPill, Props }
import akka.pattern.{ Backoff, BackoffSupervisor }

import scala.concurrent.duration.FiniteDuration
import scala.util.control.NoStackTrace
import scala.concurrent.duration._

class TestException extends RuntimeException with NoStackTrace

object PrintActor {
  def props: Props = Props(new PrintActor)
}

class PrintActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case "boom" => throw new TestException
    case msg: String  =>
      log.info("Receive message: {}", msg)
  }

  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    super.postStop()
    log.info("post stop")
  }

  @scala.throws[Exception](classOf[Exception])
  override def postRestart(reason: Throwable): Unit = {
    super.postRestart(reason)
    log.info("post restart")
  }
}

object ApplicationMain extends App {
  val system = ActorSystem("MyActorSystem")

  val supervisor = system.actorOf(
    BackoffSupervisor.props(
      Backoff.onFailure(
        childProps = PrintActor.props,
        childName = "print-actor",
        minBackoff = 100.millis,
        maxBackoff = 3.seconds,
        randomFactor = 0.2
      )
    )
  )

  supervisor ! PoisonPill



  Thread.sleep(1000)
  system.terminate()
}
