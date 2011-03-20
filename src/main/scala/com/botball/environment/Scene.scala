package com.botball.environment

import collection.immutable.HashMap

/**
 * Represents the simulation scene that contains the simulated nodes.
 */
class Scene {
  var nodes:List[Node] = List()

  def registerNode(node: Node): List[Node] = {
    nodes = node :: nodes
    nodes
  }

  def unRegisterNode(node:Node): List[Node] = {
    nodes = nodes.filterNot(n => n == node)
    nodes
  }

  def updateScene(timetick:TimeTick) {
    nodes = nodes.map(node => node.evaluate(timetick))
  }

  def readSensorData(): List[(Node, List[SensorData])] = {
    nodes.map( node => (node, node.sense) )
  }
}
