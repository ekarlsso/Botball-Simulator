package com.botball.environment

import scalala.tensor.dense.DenseVector

trait Static { this: Node =>
  protected def calculatePosition(timeDiff: Long): DenseVector[Double] = position
}

trait ConstantVelocityMovement { this: Node =>
  protected def calculatePosition(timeDiff: Long): DenseVector[Double] = position :+ (velocity :* timeDiff.toDouble)
  protected def velocity: DenseVector[Double] = DenseVector(0.0, 0.0, 0.0)
}

trait Evaluable extends Node {
  def evaluate(timeDiff: Long, timeTick: Long = 1) = new Node(calculatePosition(timeDiff))
  protected def calculatePosition(timeDiff: Long): DenseVector[Double]
}

class Node(pos: DenseVector[Double] = DenseVector(0.0, 0.0, 0.0)) {
  def position = pos

  def evaluate(timetick:TimeTick) = this
}
