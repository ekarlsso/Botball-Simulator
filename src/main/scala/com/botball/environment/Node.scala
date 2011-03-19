package com.botball.environment

import scalala.tensor.dense.DenseVector

class Node(pos: DenseVector[Double] = DenseVector(0.0, 0.0, 0.0)) {
  
  val position = pos

  var controllers:List[Controller] = List()

  def apply(c: Controller, timeDiff: Double) = {
    new Node(c.positionController(timeDiff, this))
  }

  def applyControllers(timeDiff:Long, timeTick:Long) = {
    controllers.foreach(controller => {
      this.apply(controller, timeDiff.toDouble)  //TODO: Timediference should be long!
    })
  }
}
