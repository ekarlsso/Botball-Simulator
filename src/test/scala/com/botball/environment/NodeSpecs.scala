package com.botball.environment

import com.botball.math._
import org.specs.runner.JUnit4
import org.specs.Specification
import scalala.tensor.dense.DenseVector

class RobotNodeSpecsAsTests extends JUnit4(RobotNodeSpecs)

object RobotNodeSpecs extends Specification {

  "RobotNode" should {
    "respond to Forward command" in {
      testNode(1.0, Vec2(3.0, 5.0))
        .handlesCommand(new MoveForwardCommand()) must beTrue
    }

    "respond to RotateLeftCommand command" in {
      testNode(1.0, Vec2(3.0, 5.0))
        .handlesCommand(new RotateLeftCommand()) must beTrue
    }

    "respond to RotateRightCommand command" in {
      testNode(1.0, Vec2(3.0, 5.0))
        .handlesCommand(new RotateRightCommand()) must beTrue
    }

    "move straight constant velocity" in {
      testNode(1.0, Vec2(3.0, 5.0))
        .evaluate(timeDiff(4), new MoveForwardCommand())
        .position.toList must beEqualTo(Vec2(7.0, 5.0).toList)

      testNode(5.0, Vec2(4.0, 2.0))
        .evaluate(timeDiff(4), new MoveForwardCommand())
        .position.toList must beEqualTo(Vec2(24.0, 2.0).toList)

      testNode(-5.0, Vec2(4.0, 2.0))
        .evaluate(timeDiff(4), new MoveForwardCommand())
        .position.toList must beEqualTo(Vec2(-16.0, 2.0).toList)
    }

    "move straight after rotation" in {
      val position =
        testNode(1.0, Vec2(3.0, 5.0), 90.0)
          .evaluate(timeDiff(4), new MoveForwardCommand()).position

      position.x must beCloseTo(3.0, 0.001)
      position.y must beCloseTo(9.0, 0.001)

      val position2 =
        testNode(1.0, Vec2(3.0, 5.0), 45.0)
          .evaluate(timeDiff(4), new MoveForwardCommand()).position

      position2.x must beCloseTo(3.0+2.8284, 0.001)
      position2.y must beCloseTo(5.0+2.8284, 0.001)

      val position3 =
        testNode(1.0, Vec2(3.0, 5.0), -45.0)
          .evaluate(timeDiff(4), new MoveForwardCommand()).position

      position3.x must beCloseTo(3.0+2.8284, 0.001)
      position3.y must beCloseTo(5.0-2.8284, 0.001)
    }

    "rotate" in {
      testNode(1.0, Vec2(3.0, 5.0))
        .evaluate(timeDiff(4), new RotateLeftCommand())
        .rotation must beCloseTo(10.0*4, 0.001)

    }
  }

  def testNode(velo: Double,
               position: Vec2 = Vec2(0.0, 0.0),
               rotation: Double = 0.0): Node = {
    new RobotNode(NodeId(0), position, rotation) {
      override def velocity = velo
      override def rotationSpeed = 10.0
    }
  }

  def timeDiff(timeDiff: Long) = TimeTick(timeDiff, timeDiff)
}
