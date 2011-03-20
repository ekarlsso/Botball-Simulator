package com.botball.environment

import akka.actor._

case class ClockNotStarted()
case class StartClock(initialTime:Long)
case class StopClock()
case class TimeTick(time: Long, timeDiff: Long)
case class GetCurrentTime()
case class AddSimulant(simulant:ActorRef)
case class TimeTickReady(time:Long)


class Clock extends Actor {

  private var running = false
  private var currentTime: Long = 0;
  private var previousTime: Long = 0;
  private var simulants:List[ActorRef] = List()

  def addSimulant(simulant:ActorRef) = {
    simulants = simulant :: simulants
  }

  def addRobot(robot:Robot) = {
    
  }

  def sendTimeTick() {
    simulants.foreach( (a:ActorRef) => {a ! TimeTick(currentTime, currentTime - previousTime)})
  }

  def receive = {
    case StartClock(initialTime) =>
      if (!running) {
        running = true
        currentTime = initialTime
        previousTime = initialTime
        sendTimeTick()
      }

    case StopClock => running = false

    case AddSimulant(simulant) => 
      addSimulant(simulant)

    case GetCurrentTime =>
      if (running) {
        self.reply(TimeTick(currentTime, currentTime - previousTime))
        previousTime = currentTime
      } else {
        self.reply(ClockNotStarted)
      }

    case TimeTickReady(value) =>
      if (value == currentTime) {
        
      }
      
    case _ => log.error("Clock got unknown message")
  }
}
