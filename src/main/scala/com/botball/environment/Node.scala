package com.botball.environment

import com.botball.math._

case class NodeId(value:Long)
case class MoveForwardCommand()
case class RotateLeftCommand()
case class RotateRightCommand()


trait ForwardCommandHandler {

  this: Node =>

  protected def velocity = 0.0

  protected def moveForward(timeTick: TimeTick): Node = {
    createNewNode(
      this.nodeId,
      calculcatePosition(timeTick),
      this.rotation,
      this.boundingBox)
  }

  protected def calculcatePosition(timeTick: TimeTick): Vec2 = {

    val radianAngle = math.toRadians(rotation)
    val velocityVec =
      Vec2(1.0, 0).rotate(radianAngle) multiply (velocity * timeTick.timeDiff)

    this.position add velocityVec
  }

  protected def handleForwardCommand: PartialFunction[Any, Function[TimeTick, Node]] = {
    case e: MoveForwardCommand => moveForward _
  }
}

trait RotateCommandHandler {

  this: Node =>

  def rotationSpeed: Double = 0.0

  protected def rotate(timeTick: TimeTick, newRotation: Double): Node = {
    createNewNode(
      this.nodeId,
      this.position,
      newRotation * timeTick.timeDiff,
      this.boundingBox)
  }

  protected def handleRotateCommand: PartialFunction[Any, Function[TimeTick, Node]]  = {
    case e: RotateLeftCommand => rotate(_, rotationSpeed)
    case e: RotateRightCommand => rotate(_, -rotationSpeed)
  }
}

/**
 * Abstract base class for all nodes. To create a node with capabilities extend
 * this class and provide partial functions for commandHandler to handle 
 * commands for each time tick
 */
abstract class Node(id: NodeId, pos: Vec2, rot: Double, bbBox: Vec2) extends DefaultSensing {

  /**
   * Unique Node ID
   */
  def nodeId = id

  /**
   * Position of the node in the  scene
   */
  def position = pos

  /**
   * Rotation angle (in degrees) of the node. Counter Clock wise rotation
   */
  def rotation = rot

  /**
   * Dimensions of the node.
   */
  def boundingBox = bbBox

  /**
   * Returns JSON representation of the node
   */
  def toJSON =
    "{\"type\": \"Node\", "+
    "\"position\":["+position(0)+ ", "+position(1)+"], " +
    "\"rotation\":"+rotation+", " +
    "\"boundingBox\":["+boundingBox(0)+ ", "+boundingBox(1)+"]}"

  /**
   * Returns true if the node understand the given command
   */
  def handlesCommand(command: Any) = handleCommand.isDefinedAt(command)

  /**
   * Handles the given command. Partial function, abstract and should be
   * implemented by the derived class
   */
  def handleCommand: PartialFunction[Any, Function[TimeTick, Node]]

  /**
   * Called for each simulation time tick. In each time tick the node should
   * produce a new node (transform) that matches the state transformation done
   * based on the applied (command
   */
  def evaluate(timeTick: TimeTick, command: Any): Node = 
    if (handlesCommand(command)) handleCommand(command)(timeTick) else this

  protected def createNewNode(id: NodeId, pos: Vec2, rot: Double, bbBox: Vec2): Node
}

class RobotNode(id: NodeId,
                pos: Vec2 = Vec2(0,0),
                rot: Double = 0.0,
                bbBox: Vec2 = Vec2(30.0, 30.0))
  extends Node(id, pos, rot, bbBox)
  with ForwardCommandHandler
  with RotateCommandHandler {

  override def rotationSpeed = 2.0

  override def velocity = 2.0

  override def handleCommand =
    handleForwardCommand orElse
    handleRotateCommand

  override protected def createNewNode(id: NodeId, pos: Vec2, rot: Double, bbBox: Vec2): Node =
    new RobotNode(id, pos, rot, bbBox)
}

trait NodeFactor {

  var uniqueIdCounter = 0;

  def createNode(position: Vec2 = Vec2(0.0, 0.0),
                 rotation: Double = 0.0,
                 bbBox: Vec2 = Vec2(30.0, 30.0)): Node = {
    uniqueIdCounter += 1;
    new RobotNode(NodeId(uniqueIdCounter), position, rotation, bbBox);
  }
}
