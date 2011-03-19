package com.botball.environment

import scalala.tensor.dense.DenseVector

class Node(pos: DenseVector[Double] = DenseVector(0.0, 0.0, 0.0)) {
  
  val position = pos

  var controllers:List[Controller] = List()

  def apply(c: Controller, timeDiff: Long) = {
    c.apply(this, timeDiff)
  }

  def applyControllers(timeDiff:Long, timeTick:Long): Node = {

    var tempNode = this

    controllers.foreach(controller => {
      tempNode = tempNode.apply(controller, timeDiff)
    })

    tempNode
  }
}
