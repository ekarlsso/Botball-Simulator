package com.botball.environment

import com.botball.math._

trait NodeFactor {
  def createNode(nodeType: String,
                 position: Vec2  = Vec2(0.0, 0.0),
                 rotation: Double = 0.0,
                 boundingBox: Vec2 = Vec2(30.0, 30.0)): Node
}

class DefaultNodeFactor extends NodeFactor {

  var uniqueIdCounter = 0;

  def createNode(nodeType: String,
                 position: Vec2,
                 rotation: Double,
                 bbBox: Vec2): Node = {

    nodeType match {
      case "RobotNode" => newRobotNode(position, rotation, bbBox)
      case _ => null
    }
  }

  protected def newRobotNode(position: Vec2, rotation: Double, bbBox: Vec2): Node  = {
    uniqueIdCounter += 1
    new RobotNode(NodeId(uniqueIdCounter), position, rotation, bbBox);
  }
}
