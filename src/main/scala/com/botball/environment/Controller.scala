package com.botball.environment

import scalala.tensor.dense.DenseVector

abstract class Controller {
  def positionController(timeDiff: Double, node: Node) : DenseVector[Double]
}

class ConstantVelocityController(velocity: DenseVector[Double]) extends Controller {
  override def positionController(timeDiff: Double, node: Node) : DenseVector[Double] =
    node.position :+ (velocity :* timeDiff)
}