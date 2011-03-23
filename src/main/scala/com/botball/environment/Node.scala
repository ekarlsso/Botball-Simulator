package com.botball.environment

import scalala.tensor.dense.DenseVector

trait Static {
  this: Node =>
  protected def calculatePosition(timeDiff: Long): DenseVector[Double] = position
}

trait ConstantVelocityMovement {
  this: Node =>

  protected def calculatePosition(timeDiff: Long): DenseVector[Double] =
    position :+ (velocity :* timeDiff.toDouble)

  protected def velocity: DenseVector[Double] = DenseVector(0.0, 0.0)
}

trait Animated {
  this: Node =>

  override def evaluate(timetick: TimeTick) =
    new Node(calculatePosition(timetick.timeDiff), this.rotation)

  protected def calculatePosition(timeDiff: Long): DenseVector[Double]
}

class Node(pos: DenseVector[Double] = DenseVector(0.0, 0.0),
           rot: Double = 0.0,
           bbBox:DenseVector[Double] = DenseVector(30.0, 30.0))
  extends DefaultSensing {

  def position = pos

  def rotation = rot

  def boundingBox = bbBox

  def evaluate(timetick: TimeTick) = this

  def toJSON = "{\"type\": \"Node\", "+
    "\"position\":["+position(0)+ ", "+position(1)+"], " +
    "\"rotation\":"+rotation.toString+", " +
    "\"boundingBox\":["+boundingBox(0)+ ", "+boundingBox(1)+"]}"
}

class RobotNode(pos: DenseVector[Double] = DenseVector(0.0, 0.0),
                rot: Double = 0.0,
                bbBox: DenseVector[Double] = DenseVector(30.0, 30.0),
                vel: DenseVector[Double] = DenseVector(0.0, 0.0))
  extends Node(pos, rot, bbBox) with Animated with ConstantVelocityMovement {
  
  override def velocity = vel
}
