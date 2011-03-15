package com.botball.environment

import akka.actor._

case class ClockNotStarted()
case class StartClock(initialTime:Long)
case class TimeTick(time:Long)
case class GetCurrentTime()
case class AddSimulant(simulant:ActorRef)

/**
 * Created by IntelliJ IDEA.
 * User: ekarlsso
 * Date: 3/13/11
 * Time: 7:42 PM
 * To change this template use File | Settings | File Templates.
 */
class Clock extends Actor {

  private var running = false
  private var currentTime:Long = 0;
  private var simulants:List[ActorRef] = List()

  def addSimulant(simulant:ActorRef) = {
    simulants = simulant :: simulants
  }
  
  def sendTimeTick() {
    simulants.foreach( (a:ActorRef) => {a ! TimeTick(currentTime)})
  }

  def receive = {
    case StartClock(initialTime) =>
      if (!running) {
        running = true
        currentTime = initialTime
        sendTimeTick()
      }

    case AddSimulant(simulant) => 
      addSimulant(simulant)

    case GetCurrentTime =>
      if (running) {
        self.reply(TimeTick(currentTime))
      } else {
        self.reply(ClockNotStarted)
      }
      
    case _ => log.error("Clock got unknown message")
  }
}

