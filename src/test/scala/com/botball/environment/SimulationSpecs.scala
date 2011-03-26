package com.botball.environment

import akka.util.TestKit
import akka.util.duration._
import akka.actor._
import akka.actor.Actor.actorOf
import org.specs._
import org.specs.runner.JUnit4
import org.specs.mock.Mockito
import org.mockito.Matchers._
import com.botball.math._

class SimulationSpecsAsTests extends JUnit4(SimulationSpecs)

object SimulationSpecs extends Specification with Mockito with TestKit {


  class RobotRegistry(sceneObj: Scene = new Scene,
                      nodeFac: NodeFactor = new DefaultNodeFactor()) extends SceneManagement {
    override def sendRobotsReply() {}
    override def informRobots(timeTick: TimeTick) {}
    override def sendSensorData(actor: ActorRef, sensorData: List[SensorData]) {}
    override def scene = sceneObj
    
    def nodeFactor: NodeFactor = nodeFac
    def actorRef = null
  }

  "SceneManagement" should {

    "be able to register and unregister robots" in {

      val sceneMock = mock[Scene]
      val node1 = mock[Node]
      val node2 = mock[Node]
      val robot1 = mock[ActorRef]
      val robot2 = mock[ActorRef]

      val roboRegistry = new RobotRegistry(sceneMock)
      val roboRegistrySpy = spy(roboRegistry)

      roboRegistrySpy.registerRobot(RegisterRobot(robot1))
      roboRegistrySpy.registerRobot(RegisterRobot(robot2))

      there was two(sceneMock).registerNode(any[Node])

      roboRegistrySpy.registeredRobots.length must_== 2
      roboRegistrySpy.registeredRobots.contains(robot1) must beTrue
      roboRegistrySpy.registeredRobots.contains(robot2) must beTrue

      roboRegistrySpy.unRegisterRobot(UnRegisterRobot(robot1))
      roboRegistrySpy.registeredRobots.contains(robot1) must beFalse
      roboRegistrySpy.registeredRobots.contains(robot2) must beTrue

      roboRegistrySpy.unRegisterRobot(UnRegisterRobot(robot2))
      roboRegistrySpy.registeredRobots.contains(robot1) must beFalse
      roboRegistrySpy.registeredRobots.contains(robot2) must beFalse

      there was two(sceneMock).unRegisterNode(any[NodeId])
    }

    "update simulation state" in {

      val sceneMock = mock[Scene]
      val node1 = mock[Node]
      val node2 = mock[Node]
      val robot1 = mock[ActorRef]
      val robot2 = mock[ActorRef]
      val sensorData1 = mock[SensorData]
      val sensorData2 = mock[SensorData]

      val roboRegistry = new RobotRegistry(sceneMock)
      
      val roboRegistrySpy = spy(roboRegistry)

      //Create the stubs
      node1.nodeId returns NodeId(0)
      node1.nodeId returns NodeId(1)

      roboRegistrySpy.createRobotNode(RegisterRobot(robot1)) returns node1
      roboRegistrySpy.createRobotNode(RegisterRobot(robot2)) returns node2

      sceneMock.readSensorData returns
        List((node1, List(sensorData1)), (node2, List(sensorData2)))

      //Setup the test
      roboRegistrySpy.registerRobot(RegisterRobot(robot1))
      roboRegistrySpy.registerRobot(RegisterRobot(robot2))

      //Update the state
      val timeTick = TimeTick(10,20)
      roboRegistrySpy.updateSimulationState(timeTick)

      there was one(sceneMock).updateScene(timeTick)
      there was one(sceneMock).readSensorData()

      there was one(roboRegistrySpy).sendSensorData(robot1, List(sensorData1))
      there was one(roboRegistrySpy).sendSensorData(robot2, List(sensorData2))
      there was one(roboRegistrySpy).informRobots(timeTick)
    }
  }

  "Simulation" should {

    var simulation:ActorRef = null

    doAfter {
      simulation.stop
    }

    doLast {
      stopTestActor
    }

    "Advance state with new clock tick" in {
      testActor.isRunning must beTrue

      simulation = Actor.actorOf(new Simulation(new DefaultNodeFactor) {
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
