package com.botball.environment

import scalala.tensor.dense.DenseVector

abstract class Controller {

  def apply(node:Node, timeDiff:Long): Node

  /**
   * @deprecated use apply instead
   */
  def positionController(timeDiff: Double, node: Node) : DenseVector[Double]
}

class ConstantVelocityController(velocity: DenseVector[Double]) extends Controller {

  override def apply(node:Node, timeDiff:Long) : Node = {
    new Node(node.position :+ (velocity :* timeDiff.toDouble) )
  }

  override def positionController(timeDiff: Double, node: Node) : DenseVector[Double] =
    node.position :+ (velocity :* timeDiff)
}