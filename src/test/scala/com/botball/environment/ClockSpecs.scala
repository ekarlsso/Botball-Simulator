package com.botball.environment

import akka.util.TestKit
import akka.util.duration._
import akka.actor._
import akka.actor.Actor.actorOf
import org.specs._
import org.specs.runner.JUnit4
import org.specs.mock.Mockito
import org.mockito.Matchers._


class ClockSpecsAsTests extends JUnit4(ClockSpecs)

object ClockSpecs extends Specification with Mockito  with TestKit {
  
  "The Clock" should {
  
    doLast {
      stopTestActor
    }
  
    "return the current time when running" in {
      val clock = Actor.actorOf[Clock]
      clock.start

      val firstResult  = clock !! (GetCurrentTime, 1000)
      (firstResult match {
        case Some(ClockNotStarted) => "clock-is-non-started"
        case Some(TimeTick(time)) => "Clock shouldn't be started"
        case _ => "Clock returning undefined event"
      }) must_== "clock-is-non-started"

      clock ! StartClock(10)

      val result = clock !! (GetCurrentTime, 1000)

      result match {
        case Some(TimeTick(time)) => time must_== 10
        case _ => fail("Not returning TimeTick(time)")
      }
    }

    "return the current time when running2" in {
    
      //For some reason this is needed before this gets working
      //do we have some lazy loading here?
      testActor.isRunning must beTrue 
    
      val clock = Actor.actorOf[Clock]
      clock.start()    
    
      within(1000 millis) {
        clock ! StartClock(10)
        clock ! GetCurrentTime
        expectMsg(TimeTick(10))
      }
    }
  }
}
