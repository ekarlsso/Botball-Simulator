package com.botball.environment

import scala.collection.immutable.HashMap
import akka.actor.ActorRef
import akka.actor._

case class StartSimulation()
case class StopSimulation()
case class RegisterRobot(robot:ActorRef)
case class UnRegisterRobot(robot:ActorRef)
case class SensorDataEvent()

class SimulationClass(scene:Scene = new Scene) {

  private var robots:List[ActorRef] = List()
  private var robotsMap:HashMap[Node, ActorRef] = new HashMap[Node, ActorRef]
  private var simulationRunning = false
  private var previousTimeTick:Long = 0

  private val clock = Actor.actorOf[Clock]

  def registeredRobots = robots

  def registerRobot(robot:ActorRef):List[ActorRef]  = {

    if (robots.exists(r => r == robot)) return robots

    robots = robot :: robots

    val node = new Node
    robotsMap = robotsMap + (node -> robot)
    scene.registerNode(node)
    robots
  }

  def unRegisterRobot(robot:ActorRef): List[ActorRef] = {

    if (!robots.exists(r => r == robot)) return robots

    robots = robots.filterNot(rob => rob == robot)

    var removedNode:Node = null
    robotsMap = robotsMap.filter( mp => {
      if (mp._2 == robot) {
        removedNode = mp._1
        false
      } else {
        true
      }
    })

    scene.unRegisterNode(removedNode)

    robots
  }

  def simulationClockTick(timetick:TimeTick) {

    scene.updateScene(timetick)

    scene.readSensorData.foreach(data => {
      val node = data._1
      val sensorData = data._2

      val robotRef = robotsMap(node)
      sendSensorData(robotRef, sensorData)
    })

    previousTimeTick = timetick.time
  }

  def startSimulation() {

    if (simulationRunning) return

    simulationRunning = true
    clock.start
  }

  def stopSimulation() {

    if (!simulationRunning) return

    simulationRunning = false
    clock.stop
  }

  protected def sendSensorData(actor:ActorRef, sensorData:SensorDataEvent) {
    actor ! sensorData
  }
}


trait SimulationActor extends Actor {

  this: Simulation =>

  def receive = {
    case StartSimulation => 
      this.startSimulation()

    case StopSimulation =>
      this.stopSimulation()
      
    case RegisterRobot(robot) =>
      this.registerRobot(robot)

    case UnRegisterRobot(robot) =>
      this.unRegisterRobot(robot)

    case timetick: TimeTick =>
      this.simulationClockTick(timetick)

    case _ => {
      log.error("Simulation got unknown message")
    }
  }
}

class Simulation extends SimulationClass with SimulationActor 