package com.botball.environment

import se.scalablesolutions.akka.actor._
import se.scalablesolutions.akka.actor.Actor.actorOf
import org.specs._
import org.specs.runner.JUnit4


class ClockSpecsAsTests extends JUnit4(ClockSpecs)

object ClockSpecs extends Specification {

  "The Clock" should {
    "return the current time" in {
      val clock = Actor.actorOf[Clock]
      clock.start

      var option = clock !! (new GetCurrentTime, 1000)
      //option.getOrElse()
    }
  }
}
