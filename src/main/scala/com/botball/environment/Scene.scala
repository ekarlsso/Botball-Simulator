package com.botball.environment

import collection.immutable.HashMap

/**
 * Represents the simulation scene that contains the simulated nodes.
 */
class Scene {
  var sceneNodes:List[Node] = List()

  def registerNode(node: Node): List[Node] = {
    sceneNodes = node :: sceneNodes
    sceneNodes
  }

  def unRegisterNode(nodeId:NodeId): List[Node] = {
    sceneNodes = sceneNodes.filterNot(n => n.nodeId == nodeId)
    sceneNodes
  }

  def updateScene(timetick:TimeTick) {
    //sceneNodes = sceneNodes.map(node => node.evaluate(timetick, ))
  }

  def readSensorData(): List[(Node, List[SensorData])] = {
    nodes.map(node => (node, node.sense) )
  }

  def nodes = sceneNodes
}
