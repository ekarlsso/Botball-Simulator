package com.botball.web

import com.botball.environment._
import com.botball.util._
import akka.actor._
import org.scalatra._
import java.net.URL

class SimulationRESTFilter extends ScalatraFilter {

  val nodeFactor = new DefaultNodeFactor
  val simulation = Actor.actorOf(new Simulation(nodeFactor)).start
  val robot1 = Actor.actorOf[Robot].start
  val robot2 = Actor.actorOf[Robot].start

  var simulationSetuped = false

  get("/simulation/setup") {

    if (!simulationSetuped) {

      simulation ! RegisterRobot(robot1)
      simulation ! RegisterRobot(robot2, (50,50))

      simulationSetuped = true

      statusMessage("ok", "Simulation setup done")
    } else {
      statusMessage("ok", "Simulation already have a setup")
    }
  }

  get("/simulation/start") {
    simulation ! StartSimulation()
    statusMessage("ok", "Simulation started")
  }

  get("/simulation/stop") {
    simulation ! StopSimulation()
    statusMessage("ok", "Simulation stopped")
  }

  get("/simulation/scene/robots") {
    (simulation !! (GetSceneRobots(), 1000))  match {
      case Some(robots: List[Node]) => JSONParser.nodesToJSON(robots)
      case Some(value) => "We got something from simulator..."
      case _ => "{\"status\": \"Error: couldn't retrieve robots\"}"
    }
  }

  protected def statusMessage(status: String, message: String): String =
    "{\"status\":\""+status+"\", "+
     "\"message\":\""+message+"\"}"
}
