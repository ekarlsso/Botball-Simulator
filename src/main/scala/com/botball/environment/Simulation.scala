package com.botball.environment

import scala.collection.immutable.HashMap


case class StartSimulation()
case class StopSimulation()

class Simulation(scene:Scene, clock:Clock) extends SimulationActor {

  private var robots:List[Robot] = List()
  private var robotsMap:HashMap[Robot, Node] = new HashMap[Robot, Node]
  private var simulationRunning = false
  private var previousTimeTick:Long = 0

  def registerRobot(robot:Robot):List[Robot]  = {
    robots = robot :: robots

    val node = new Node
    robotsMap = robotsMap + (robot -> node)
    scene.registerNode(node)
    robots
  }

  def unRegisterRobot(robot:Robot): List[Robot] = {
    robots = robots.filterNot(rob => rob == robot)

    val node = robotsMap(robot)
    scene.unRegisterNode(node)

    robotsMap = robotsMap - robot

    robots
  }

  def simulationClockTick(timeTick:TimeTick) {

    scene.advanceSimulation(timeTick.time - previousTimeTick, timeTick.time)
    scene.

    previousTimeTick = timeTick.time
  }

  def startSimulation() {

    if (!simulationRunning) {

      robots.foreach(robots = > {
        clock.addRobot(robot)
      })


      clock ! StartClock(0)
    }
  }


  def receive = {

  }
}

