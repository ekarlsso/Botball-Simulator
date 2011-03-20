package com.botball.environment

import scala.collection.immutable.HashMap
import akka.actor.ActorRef
import akka.actor._

case class StartSimulation()
case class StopSimulation()
case class RegisterRobot(robot:ActorRef)
case class UnRegisterRobot(robot:ActorRef)


/**
 * Manages robot registering and the scene state change related to it.
 */
trait RobotRegistryManagement {

  private var robots:List[ActorRef] = List()
  private var robotsMap:HashMap[Node, ActorRef] = new HashMap[Node, ActorRef]

  def registerRobot(event: RegisterRobot): List[ActorRef]  = {

    if (robots.exists(r => r == event.robot)) return robots

    robots = event.robot :: robots

    val node = createRobotNode(event)
    robotsMap = robotsMap + (node -> event.robot)
    scene.registerNode(node)
    robots
  }

  def unRegisterRobot(event: UnRegisterRobot): List[ActorRef] = {

    if (!robots.exists(r => r == event.robot)) return robots

    robots = robots.filterNot(robot => robot == event.robot)

    var removedNode:Node = null
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

  def robotRegistryManagement: Actor.Receive = {
    case event: RegisterRobot => this.registerRobot(event)
    case event: UnRegisterRobot => this.unRegisterRobot(event)
  }

  def registeredRobots = robots

  def robotForNode(node: Node): ActorRef = robotsMap(node)

  def scene:Scene

  protected def createRobotNode(robot:RegisterRobot) : Node = new Node
}

trait TimeTickManagement  {

  private var simulationRunning = false
  private var previousTimeTick:TimeTick = new TimeTick(0,0)

  def simulationClockTick(timetick:TimeTick) {

    scene.updateScene(timetick)

    scene.readSensorData.foreach(data => {
      val node = data._1
      val sensorData = data._2

      val robotRef = robotForNode(node)
      sendSensorData(robotRef, sensorData)
    })

    previousTimeTick = timetick
  }

  def timeTickManagement: Actor.Receive = {
    case event: TimeTick => simulationClockTick(event)
  }

  protected def sendSensorData(actor: ActorRef, sensorData: List[SensorData]){
    actor ! sensorData
  }
  
  def robotForNode(node: Node): ActorRef
  
  def scene: Scene
}

/**
 * Manages common simulation events. This include Start and Stop events
 */
trait SimulationManagement {

  var simulationRunning = false

  def startSimulation() {
    if (simulationRunning) return

    simulationRunning = true
    clock.start

    clock ! AddSimulant(simulation)
    clock ! StartClock(0)
  }

  def stopSimulation() {
    if (!simulationRunning) return

    simulationRunning = false
    clock.stop
  }

  def simulationManagement: Actor.Receive = {
    case StartSimulation => startSimulation()
    case StopSimulation => stopSimulation()
  }

  def clock: ActorRef

  def simulation: ActorRef
}

/**
 * Trait to handle unknown events
 */
trait UnknownEventManagement {
  def unknownEventManagement: Actor.Receive = {
    case _ => println("Unkown event received by Simulation")
  }
}

/**
 * Actor coodrinating the simulation
 */
class Simulation extends Actor
  with TimeTickManagement
  with RobotRegistryManagement
  with SimulationManagement
  with UnknownEventManagement {

  private var simulatedScene:Scene = new Scene
  private var simulationClock:ActorRef = Actor.actorOf[Clock]

  def scene:Scene = simulatedScene
  def clock:ActorRef = simulationClock
  def simulation:ActorRef = this.self

  def receive =
    robotRegistryManagement orElse
    timeTickManagement orElse
    simulationManagement orElse
    unknownEventManagement
}
