package com.botball.environment

import com.botball.math._
import scala.collection.immutable.HashMap
import akka.actor.ActorRef
import akka.actor._

case class StartSimulation()
case class StopSimulation()
case class RegisterRobot(robot:ActorRef, position: (Int, Int) = (0,0))
case class UnRegisterRobot(robot:ActorRef)
case class GetSceneRobots()


/**
 * Manages scene state for simulation and also for the registered robots
 */
trait SceneManagement {

  private var simulatedScene = new Scene
  private var robots: List[ActorRef] = List()
  private var robotsMap = new HashMap[NodeId, ActorRef]

  def registerRobot(event: RegisterRobot): List[ActorRef]  = {

    if (robots.exists(r => r == event.robot)) return robots

    robots = event.robot :: robots

    val node = createRobotNode(event)

    robotsMap += (node.nodeId -> event.robot)
    scene.registerNode(node)

    robots
  }

  def unRegisterRobot(event: UnRegisterRobot): List[ActorRef] = {

    if (!robots.exists(r => r == event.robot)) return robots

    robots = robots.filterNot(robot => robot == event.robot)

    var removedNode:NodeId = null
    robotsMap = robotsMap.filter( mp => {
      if (mp._2 == event.robot) {
        removedNode = mp._1
        false
      } else {
        true
      }
    })

    scene.unRegisterNode(removedNode)

    robots
  }

  def updateSimulationState(timeTick: TimeTick) {
    scene.updateScene(timeTick)

    scene.readSensorData.foreach(data => {
      val node = data._1
      val sensorData = data._2

      val robotRef = robotForNode(node)
      sendSensorData(robotRef, sensorData)
    })

    informRobots(timeTick)
  }

  def sendRobotsReply() {
    actorRef.reply(scene.nodes)
  }

  def informRobots(timeTick: TimeTick) {
    registeredRobots.foreach(robot => robot ! timeTick)
  }

  def registeredRobots = robots

  def robotForNode(node: Node): ActorRef = robotsMap(node.nodeId)

  def sendSensorData(actor: ActorRef, sensorData: List[SensorData]) {
    actor ! sensorData
  }

  def createRobotNode(robot: RegisterRobot) : Node =
    nodeFactor.createNode(
      "RobotNode",
      Vec2(robot.position._1, robot.position._2))

  def scene:Scene = simulatedScene

  def sceneManagement: Actor.Receive = {
    case event: RegisterRobot => this.registerRobot(event)
    case event: UnRegisterRobot => this.unRegisterRobot(event)
    case event: GetSceneRobots => this.sendRobotsReply()
  }

  def actorRef: ActorRef

  def nodeFactor: NodeFactor
}

/**
 * Manages simulation clock tick. This includes listening to the simulation
 * internal clock and informing the other parts of the simulation (like robot
 * actors and the Scene) that new clock tick has happened.
 *
 * Controls also starting and shutdown of the simulation
 */
trait SimulationManagement  {

  private var simulationRunning = false
  private var simulationClock = Actor.actorOf[Clock].start
  private var previousTimeTick = new TimeTick(0,0)

  def simulationClockTick(timeTick: TimeTick) {

    updateSimulationState(timeTick)

    previousTimeTick = timeTick

    replyToClockTick(timeTick)
  }

  def startSimulation() {
    if (isSimulationRunning) {
      clock ! StopClock()
    }

    simulationRunning = true

    clock ! AddSimulant(actorRef)
    clock ! StartClock(0)
  }

  def stopSimulation() {
    if (!isSimulationRunning) return

    clock ! StopClock();
    simulationRunning = false
  }

  def replyToClockTick(timeTick: TimeTick) {
    clock ! TimeTickReady(timeTick.time)
  }

  def isSimulationRunning = simulationRunning

  def clock: ActorRef = simulationClock

  def simulationManagement: Actor.Receive = {
    case event: TimeTick => simulationClockTick(event)
    case StartSimulation => startSimulation()
    case StopSimulation => stopSimulation()
  }

  def updateSimulationState(timeTick: TimeTick)

  def actorRef: ActorRef
}

/**
 * Manages unknown events.
 */
trait UnknownEventManagement {
  this: Actor =>
  def unknownEventManagement: Actor.Receive = {
    case _ => log.error("Unkown event received by Simulation")
  }
}

/**
 * Manages the simulation. Stores the simulation state and keeps track of the
 * registered robots to the simulation
 */
class Simulation(nodeFac: NodeFactor) extends Actor
  with SceneManagement
  with SimulationManagement
  with UnknownEventManagement {

  def actorRef: ActorRef = this.self

  def nodeFactor: NodeFactor = nodeFac

  def receive =
    sceneManagement orElse
    simulationManagement orElse
    unknownEventManagement
}
