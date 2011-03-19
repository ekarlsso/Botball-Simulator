package com.botball.environment

import scalala.tensor.dense.DenseVector

class Node(pos: DenseVector[Double] = DenseVector(0.0, 0.0, 0.0)) {
  
  val position = pos

  def apply(c: Controller, timeDiff: Double) = {
    new Node(c.positionController(timeDiff, this))
  }
}
