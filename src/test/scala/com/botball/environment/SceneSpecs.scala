package com.botball.environment

import org.specs._
import org.specs.runner.JUnit4
import org.specs.mock.Mockito
import org.mockito.Matchers._
import com.botball.math._

class SceneSpecAsTests extends JUnit4(SceneSpecs)

object SceneSpecs extends Specification with Mockito {

  "The Scene" should {

    "Register and Unregister nodes in scene" in {

      val scene = new Scene

      scene.registerNode(testNode(NodeId(0)))
      scene.findNode(NodeId(0)) must notBeNull

      scene.registerNode(testNode(NodeId(1)))
      scene.findNode(NodeId(1)) must notBeNull

      scene.nodes.length must_== 2

      scene.unRegisterNode(NodeId(0))
      scene.findNode(NodeId(0)) must beNull
      scene.nodes.length must_== 1

      scene.unRegisterNode(NodeId(1))
      scene.findNode(NodeId(1)) must beNull
      scene.nodes.length must_== 0
    }

    "accept posted command for node" in {

      val scene = new Scene
      var command = new MoveForwardCommand()

      scene.registerNode(testNode(NodeId(0)))
      scene.postCommand(command, NodeId(0))
      scene.nodeCommands.size must_== 1
      scene.nodeCommands(NodeId(0)).contains(command) must beTrue
    }

    "Advance node inside the scene with time tick" in {

      val scene = new Scene
      var command = new MoveForwardCommand()

      scene.registerNode(testNode(NodeId(0), 20, Vec2(5.0, 3.0)))
      scene.postCommand(command, NodeId(0))
      scene.nodeCommands(NodeId(0)).contains(command) must beTrue

      scene.updateScene(TimeTick(10, 10))

      scene.nodeCommands.contains(NodeId(0)) must beFalse
      scene.nodes.length must_== 1
      scene.nodeCommands.size must_== 0

      scene.nodes.head.position.x must_== 205.0
      scene.nodes.head.position.y must_== 3.0
    }
  }

  def testNode(nodeId: NodeId = NodeId(0),
               velo: Double = 0.0,
               position: Vec2 = Vec2(0.0, 0.0),
               rotation: Double = 0.0): Node = {
    new RobotNode(nodeId, position, rotation) {
      override def velocity = velo
      override def rotationSpeed = 10.0
    }
  }
}