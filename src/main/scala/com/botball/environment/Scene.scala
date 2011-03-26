package com.botball.environment

import collection.immutable.HashMap

/**
 * Represents the simulation scene that contains the simulated nodes.
 */
class Scene {

  var sceneNodes: List[Node] = List()
  var commands = new HashMap[NodeId, List[Command]]

  def registerNode(node: Node): List[Node] = {

    if (findNode(node.nodeId) != null) return sceneNodes

    sceneNodes = node :: sceneNodes
    sceneNodes
  }

  def unRegisterNode(nodeId:NodeId): List[Node] = {
    sceneNodes = sceneNodes.filterNot(n => n.nodeId == nodeId)
    sceneNodes
  }

  def postCommand(command: Command, nodeId: NodeId) =
    if (findNode(nodeId) != null) {
      val preCommands =
        if (commands.contains(nodeId)) commands(nodeId) else List()
      commands += (nodeId -> (command :: preCommands))
    }

  def updateScene(timeTick: TimeTick) {
    sceneNodes = sceneNodes.map(node => {
      if (commands.contains(node.nodeId)) {
        val command = commands(node.nodeId)
        if (command != List()) {
          if (command.tail != List()) {
            commands += (node.nodeId -> command.tail)
          } else {
            commands -= (node.nodeId)
          }
          node.evaluate(timeTick, command.head)
        } else {
          commands -= (node.nodeId)
          node
        }
      } else {
        node
      }
    })
  }

  def findNode(nodeId: NodeId): Node =
    sceneNodes.find(n => n.nodeId == nodeId) match {
      case Some(value) => value
      case None => null
    }

  def readSensorData(): List[(Node, List[SensorData])] = {
    nodes.map(node => (node, node.sense) )
  }

  def nodes = sceneNodes

  def nodeCommands = commands
}
