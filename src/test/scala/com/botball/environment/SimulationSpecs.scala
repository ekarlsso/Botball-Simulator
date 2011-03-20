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

  class RobotRegistry(sceneObj: Scene = new Scene) extends RobotRegistryManagement {
    def scene:Scene = sceneObj
  }

  "Simulation " should {

    "be able to register and unregister robots" in {

      val sceneMock = mock[Scene]
      val node1 = mock[Node]
      val node2 = mock[Node]
      val robot1 = mock[ActorRef]
      val robot2 = mock[ActorRef]
      
      val roboRegistry = new RobotRegistry(sceneMock) {
        override def createRobotNode(robot: RegisterRobot): Node =
          if (robot.robot == robot1) {
            node1
          } else if (robot.robot == robot2) {
            node2
          } else {
            throw new Exception("Unpsecified robot encountered")
          }
      }

      roboRegistry.registerRobot(RegisterRobot(robot1))
      there was one(sceneMock).registerNode(node1)

      roboRegistry.registerRobot(RegisterRobot(robot2))
      there was one(sceneMock).registerNode(node2)

      roboRegistry.registeredRobots.length must_== 2
      roboRegistry.registeredRobots.contains(robot1) must beTrue
      roboRegistry.registeredRobots.contains(robot2) must beTrue

      roboRegistry.unRegisterRobot(UnRegisterRobot(robot1))
      roboRegistry.registeredRobots.contains(robot1) must beFalse
      roboRegistry.registeredRobots.contains(robot2) must beTrue
      there was one(sceneMock).unRegisterNode(node1)

      roboRegistry.unRegisterRobot(UnRegisterRobot(robot2))
      roboRegistry.registeredRobots.contains(robot1) must beFalse
      roboRegistry.registeredRobots.contains(robot2) must beFalse
      there was one(sceneMock).unRegisterNode(node1)
    }
/*
    case class RobotMatcher(robot: ActorRef)

    "update scene data and collect sensordata when clock ticks" in {

      val sceneMock = mock[Scene]
      val robot1 = mock[ActorRef]
      val robot2 = mock[ActorRef]
      val node1 = mock[Node]
      val node2 = mock[Node]

      sceneMock.readSensorData returns List((node1, Nil), (node2, Nil))

      val simulation = new SimulationClass(sceneMock) {
        override def sendSensorData(actor: ActorRef, sensorData: List[SensorData]) {
             
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

      val timeTick = new TimeTick(0, 0)
      simulation.simulationClockTick(timeTick)
      there was one(sceneMock).updateScene(timeTick)
      there was one(sceneMock).readSensorData()
    }
    */
  }
}