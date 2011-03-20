package com.botball.environment

import akka.util.TestKit
import akka.util.duration._
import akka.actor._
import akka.actor.Actor.actorOf
import org.specs._
import org.specs.runner.JUnit4
import org.specs.mock.Mockito
import org.mockito.Matchers._

class SimulationSpecsAsTests extends JUnit4(SimulationSpecs)

object SimulationSpecs extends Specification with Mockito {

  "Simulation " should {

    "be able to register and unregister robots" in {

      val simulation = new SimulationClass

      val robot1 = mock[ActorRef]
      val robot2 = mock[ActorRef]

      simulation.registerRobot(robot1)
      simulation.registerRobot(robot2)

      simulation.registeredRobots.contains(robot1) must beTrue
      simulation.registeredRobots.contains(robot2) must beTrue

      simulation.unRegisterRobot(robot1)
      simulation.registeredRobots.contains(robot1) must beFalse
      simulation.registeredRobots.contains(robot2) must beTrue

      simulation.unRegisterRobot(robot2)
      simulation.registeredRobots.contains(robot1) must beFalse
      simulation.registeredRobots.contains(robot2) must beFalse
    }

    "update scene data and collect sensordata when clock ticks" in {
/*
      val sceneMock = mock[Scene]
      val simulation = new SimulationClass(sceneMock) {
        override def sendSensorData(actor:ActorRef, sensorData:SensorDataEvent) {
          
        }
      }

      val robot1 = mock[ActorRef]
      val robot2 = mock[ActorRef]

      simulation.registerRobot(robot1)
      there was one(sceneMock).registerNode(any[Node]) // <--- how to do this, is there any[Node]() or something...

      simulation.registerRobot(robot2)
      there was one(sceneMock).registerNode()
*/
      /*
      simulation.simulationClockTick(0)

      there was one(sceneMock).updateScene(0,0)
      there was one(sceneMock).readSeonsorData
      */
    }
  }

}