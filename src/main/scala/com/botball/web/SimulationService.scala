/*package com.botball.web

import com.botball.environment._
import akka.http._
import akka.actor._


class SimulationService extends Actor with Endpoint {

  val serviceRoot = "/botball/"
  val provideSimulationActor = serviceRoot + "simulation"

  lazy val simulation = Actor.actorOf[Simulation].start

  self.dispatcher = Endpoint.Dispatcher

  def hook(uri: String): Boolean = (uri == provideSimulationActor)

  def provide(uri: String) =
    Actor.actorOf(new SimulationRESTActor(simulation)).start

  override def preStart = {
    val root =  Actor.registry.actorsFor(classOf[RootEndpoint]).head
    root ! Endpoint.Attach(hook, provide)
  }

  def receive = handleHttpRequest
}

class SimulationRESTActor(simulator: ActorRef) extends Actor {

  def receive = {
    case get: Get => get OK "SIMULATION!!!"
    case other: RequestMethod => other NotAllowed "Unsupported request"
  }
}*/