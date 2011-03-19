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
    "Advance scene with the time tick" in {
      val scene = new Scene
      val node1Mock = mock[Node]
      val node2Mock = mock[Node]

      scene.registerNode(node1Mock)
      scene.registerNode(node2Mock)

      scene.advanceSimulation(0,0)

      there was one(node1Mock).applyControllers(0,0)
      there was one(node2Mock).applyControllers(0,0)
    }
  }
}