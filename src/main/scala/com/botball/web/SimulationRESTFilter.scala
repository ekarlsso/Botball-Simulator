package com.botball.web

import com.botball.environment._
import com.botball.util._
import akka.actor._
import org.scalatra._
import java.net.URL

class SimulationRESTFilter extends ScalatraFilter {

  val simulation = Actor.actorOf[Simulation].start
  val robot1 = Actor.actorOf[Robot].start
  val robot2 = Actor.actorOf[Robot].start

  var simulationSetuped = false

  get("/simulation/setup") {

    if (!simulationSetuped) {

      simulation ! RegisterRobot(robot1)
      simulation ! RegisterRobot(robot2, (50,50))

      simulationSetuped = true

      "Simulation setuped"

    } else {
      "Simulation already setuped!"
    }
  }

  get("/simulation/start") {
    simulation ! StartSimulation()
    "Simulation started"
  }

  get("/simulation/stop") {
    simulation ! StopSimulation()
    "Simulation stopped"
  }


  /*
  post("/simulation/robot") {

    

  }*/

  get("/simulation/scene/robots") {
    (simulation !! (GetSceneRobots(), 1000))  match {
      case Some(robots: List[Node]) => JSONParser.nodesToJSON(robots)
      case Some(value) => "We got something from simulator..."
      case _ => "{\"status\": \"Error: couldn't retrieve robots\"}"
    }
  }
}
