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

  class TestBase(sceneObj: Scene = new Scene) {
     def scene:Scene = sceneObj
  }

  class RobotRegistry(sceneObj: Scene = new Scene) extends TestBase(sceneObj)
    with RobotRegistryManagement
  
  class TimeClass(sceneObj: Scene = new Scene) extends TestBase(sceneObj)
    with TimeTickManagement {

    def robotForNode(node: Node): ActorRef  = {
      return null
    }

    override def sendSensorData(actor: ActorRef, sensorData: List[SensorData]) {

    }
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

    "update scene and read sensor data for each time tick" in {
      
      val sceneMock = mock[Scene]
      val node1 = mock[Node]
      val node2 = mock[Node]

      val robot1 = mock[ActorRef]
      val scalaActor1 = mock[ScalaActorRef]

      val robot2 = mock[ActorRef]
      val scalaActor2 = mock[ScalaActorRef]

      val sensorData1 = mock[SensorData]
      val sensorData2 = mock[SensorData]

      var timeTickObj = new TimeClass(sceneMock);

      val timeTickObjSpy = spy(timeTickObj)

      timeTickObjSpy.robotForNode(node1) returns robot1
      timeTickObjSpy.robotForNode(node2) returns robot2

      sceneMock.readSensorData returns List((node1, List(sensorData1)),
        (node2, List(sensorData2)))

      val timeTick = new TimeTick(10L, 0L)

      timeTickObjSpy.simulationClockTick(timeTick)

      there was one(sceneMock).updateScene(timeTick)
      there was one(sceneMock).readSensorData()

      there was one(timeTickObjSpy).sendSensorData(robot1, List(sensorData1))
      there was one(timeTickObjSpy).sendSensorData(robot2, List(sensorData2))
    }
  }
}
