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

object SimulationSpecs extends Specification with Mockito with TestKit {

  class TestBase(sceneObj: Scene = new Scene) {
     def scene:Scene = sceneObj
  }

  class RobotRegistry(sceneObj: Scene = new Scene) extends TestBase(sceneObj)
    with RobotRegistryManagement {

    override def sendRobotsReply() {

    }

    def simulation: ActorRef = null
  }
  
  class TimeClass(sceneObj: Scene = new Scene) extends TestBase(sceneObj)
    with TimeTickManagement {

    def robotForNode(node: Node): ActorRef  = null

    def clock: ActorRef = null

    def registeredRobots: List[ActorRef] = List()

    override def sendSensorData(actor: ActorRef, sensorData: List[SensorData]) { }

    override def informRegisteredRobots(timeTick: TimeTick) { }

    override def replyToClockTick(timeTick: TimeTick) { }
  }

  "Simulation logic" should {

    "be able to register and unregister robots" in {

      val sceneMock = mock[Scene]
      val node1 = mock[Node]
      val node2 = mock[Node]
      val robot1 = mock[ActorRef]
      val robot2 = mock[ActorRef]
      
      val roboRegistry = new RobotRegistry(sceneMock)
      val roboRegistrySpy = spy(roboRegistry)

      roboRegistrySpy.createRobotNode(RegisterRobot(robot1)) returns node1
      roboRegistrySpy.createRobotNode(RegisterRobot(robot2)) returns node2

      roboRegistrySpy.registerRobot(RegisterRobot(robot1))
      there was one(sceneMock).registerNode(node1)

      roboRegistrySpy.registerRobot(RegisterRobot(robot2))
      there was one(sceneMock).registerNode(node2)

      roboRegistrySpy.registeredRobots.length must_== 2
      roboRegistrySpy.registeredRobots.contains(robot1) must beTrue
      roboRegistrySpy.registeredRobots.contains(robot2) must beTrue

      roboRegistrySpy.unRegisterRobot(UnRegisterRobot(robot1))
      roboRegistrySpy.registeredRobots.contains(robot1) must beFalse
      roboRegistrySpy.registeredRobots.contains(robot2) must beTrue
      there was one(sceneMock).unRegisterNode(node1)

      roboRegistrySpy.unRegisterRobot(UnRegisterRobot(robot2))
      roboRegistrySpy.registeredRobots.contains(robot1) must beFalse
      roboRegistrySpy.registeredRobots.contains(robot2) must beFalse
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

      val timeTick = new TimeTick(10L, 0L)

      var timeTickObj = new TimeClass(sceneMock);
      val timeTickObjSpy = spy(timeTickObj)

      timeTickObjSpy.robotForNode(node1) returns robot1
      timeTickObjSpy.robotForNode(node2) returns robot2

      sceneMock.readSensorData returns List((node1, List(sensorData1)),
        (node2, List(sensorData2)))

      timeTickObjSpy.simulationClockTick(timeTick)

      there was one(sceneMock).updateScene(timeTick)
      there was one(sceneMock).readSensorData()

      there was one(timeTickObjSpy).sendSensorData(robot1, List(sensorData1))
      there was one(timeTickObjSpy).sendSensorData(robot2, List(sensorData2))
      there was one(timeTickObjSpy).replyToClockTick(timeTick)
      there was one(timeTickObjSpy).informRegisteredRobots(timeTick)
    }
  }

  "Simulation actor " should {

    var simulation:ActorRef = null

    doAfter {
      simulation.stop
    }

    doLast {
      stopTestActor
    }

    "Advance state with new clock tick" in {

      testActor.isRunning must beTrue

      simulation = Actor.actorOf(new Simulation {
        override def clock: ActorRef = testActor
      })
      simulation.start

      within(1000 millis) {

        simulation ! RegisterRobot(testActor)
        simulation ! TimeTick(0, 0)
        expectMsg(List())            //Empty sensor data
        expectMsg(TimeTick(0, 0))    //Current Time Tick for robot
        expectMsg(TimeTickReady(0))  //Reply To Clock

        simulation ! UnRegisterRobot(testActor)
        simulation ! TimeTick(10, 10)
        expectMsg(TimeTickReady(10))  //Reply To Clock
      }
    }
  }
}
