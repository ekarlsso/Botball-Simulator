package com.botball.environment

import akka.util.TestKit
import akka.util.duration._
import akka.actor._
import akka.actor.Actor.actorOf
import org.specs._
import org.specs.runner.JUnit4
import org.specs.mock.Mockito
import org.mockito.Matchers._


class SceneSpecAsTests extends JUnit4(ClockSpecs)

//object ClockSpecs extends Specification with Mockito  with TestKit {

object SceneSpecs extends Specification with Mockito {

  "The Scene" should {

    "Register and Unregister nodes in scene" in {
      val scene = new Scene

      val node1Mock = mock[Node]
      val node2Mock = mock[Node]

      val nodes1 = scene.registerNode(node1Mock)
      nodes1.contains(node1Mock) must beTrue

      val nodes2 = scene.registerNode(node2Mock)
      nodes2.contains(node1Mock) must beTrue
      nodes2.contains(node2Mock) must beTrue

      val nodes3 = scene.unRegisterNode(node1Mock)
      nodes3.contains(node1Mock) must beFalse

      val nodes4 = scene.unRegisterNode(node2Mock)
      nodes4.contains(node2Mock) must beFalse
    }

    "Advance nodes inside the scene with time tick" in {
      val scene = new Scene
      val node1Mock = mock[Evaluable]
      val node2Mock = mock[Evaluable]

      scene.registerNode(node1Mock)
      scene.registerNode(node2Mock)

      scene.advanceSimulation(0)

      there was one(node1Mock).evaluate(0,0)
      there was one(node2Mock).evaluate(0,0)
    }
  }
}