package com.botball.environment

import org.specs.runner.JUnit4
import org.specs.Specification
import scalala.tensor.dense.DenseVector

class ControllerSpecsAsTests extends JUnit4(ClockSpecs)

object ConstantVelocityControllerSpecs extends Specification {
  "ConstantVelocityController" should {
    "move node with constant velocity" in {
      val slowController = new ConstantVelocityController(DenseVector(1.0, 1.0, 0.0))
      val fastController = new ConstantVelocityController(DenseVector(2.0, 0.0, 1.0))
      val node = new Node
      val positionedNode = new Node(DenseVector(2.0, 1.0, 0.0))

      node.apply(slowController, 4.0).position.toList must be_==(DenseVector(4.0, 4.0, 0.0).toList)
      node.apply(slowController, 3.5).position.toList must be_==(DenseVector(3.5, 3.5, 0.0).toList)
      node.apply(fastController, 3.5).position.toList must be_==(DenseVector(7.0, 0.0, 3.5).toList)
      positionedNode.apply(fastController, 3.5).position.toList must be_==(DenseVector(9.0, 1.0, 3.5).toList)
    }
  }

}
