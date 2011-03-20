package com.botball.environment

import org.specs.runner.JUnit4
import org.specs.Specification
import scalala.tensor.dense.{DenseVectorCol, DenseVector}

class NodeSpecsAsTests extends JUnit4(ClockSpecs)

object ConstantVelocityNode extends Specification {
  class ConstantVelocityNode extends Node with Evaluable with ConstantVelocityMovement
  "ConstantVelocityNode" should {
    "move with constant velocity" in {
      testNode(DenseVector(1.0, 1.0, 0.0)).evaluate(4).position.toList must beEqualTo(DenseVector(4.0, 4.0, 0.0).toList)
      testNode(DenseVector(1.0, 1.0, 0.0)).evaluate(3).position.toList must beEqualTo(DenseVector(3.0, 3.0, 0.0).toList)
      testNode(DenseVector(2.0, 0.0, 1.0)).evaluate(3).position.toList must beEqualTo(DenseVector(6.0, 0.0, 3.0).toList)
      testNode(DenseVector(2.0, 0.0, 1.0), DenseVector(2.0, 1.0, 0.0)).evaluate(3).position.toList must beEqualTo(DenseVector(8.0, 1.0, 3.0).toList)
    }
  }
  def testNode(vel: DenseVector[Double], pos: DenseVector[Double] = DenseVector(0.0, 0.0, 0.0)) = {
    new ConstantVelocityNode {
      override def position = pos
      override protected def velocity = vel
    }
  }
}