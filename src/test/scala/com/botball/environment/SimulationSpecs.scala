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

      simulation.registerRobot(RegisterRobot(robot1))
      simulation.registerRobot(RegisterRobot(robot2))

      simulation.registeredRobots.contains(robot1) must beTrue
      simulation.registeredRobots.contains(robot2) must beTrue

      simulation.unRegisterRobot(UnRegisterRobot(robot1))
      simulation.registeredRobots.contains(robot1) must beFalse
      simulation.registeredRobots.contains(robot2) must beTrue

      simulation.unRegisterRobot(UnRegisterRobot(robot2))
      simulation.registeredRobots.contains(robot1) must beFalse
      simulation.registeredRobots.contains(robot2) must beFalse
    }

    case class RobotMatcher(robot: ActorRef)

    "update scene data and collect sensordata when clock ticks" in {

      val sceneMock = mock[Scene]
      val robot1 = mock[ActorRef]
      val robot2 = mock[ActorRef]
      val node1 = mock[Node]
      val node2 = mock[Node]

      sceneMock.readSensorData returns List((node1, new SensorDataEvent),
        (node2, new SensorDataEvent))


      val simulation = new SimulationClass(sceneMock) {
        override def sendSensorData(actor:ActorRef, sensorData:SensorDataEvent) {
             
        }

        override def createRobotNode(robot: RegisterRobot): Node =
          if (robot.robot == robot1) {
            node1
          } else if (robot.robot == robot2) {
            node2
          } else {
            throw new Exception("Unpsecified robot encountered")
          }
      }

      simulation.registerRobot(RegisterRobot(robot1))
      there was one(sceneMock).registerNode(node1)

      simulation.registerRobot(RegisterRobot(robot2))
      there was one(sceneMock).registerNode(node2)

      val timeTick = new TimeTick(0)
      simulation.simulationClockTick(timeTick)
      there was one(sceneMock).updateScene(timeTick)
      there was one(sceneMock).readSensorData()
    }
  }

}