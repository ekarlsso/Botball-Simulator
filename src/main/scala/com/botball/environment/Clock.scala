package com.botball.environment

import akka.actor._

case class ClockNotStarted()
case class StartClock(initialTime:Long)
case class StopClock()
case class TimeTick(time: Long, timeDiff: Long)
case class GetCurrentTime()
case class RegisterSimulator(simulant:ActorRef)
case class TimeTickReady(time:Long)


class Clock extends Actor {

  private val timeTickInMilliSeconds = 250
  private var previousTickInMSeconds: Long = 0


  private var running = false
  private var currentTimeTick: Long = 0;
  private var previousTimeTick: Long = 0;
  private var simulator: ActorRef = null

  def sendTimeTick() =
    if (simulator != null) {
      previousTickInMSeconds = System.currentTimeMillis()
      simulator ! TimeTick(currentTimeTick, currentTimeTick - previousTimeTick)
    }

  def receive = {
    case StartClock(initialTime) => {
      if (!running) {
        running = true
        previousTimeTick = initialTime
        previousTimeTick = initialTime
        sendTimeTick()
      }
    }
    case event: StopClock => running = false
    case RegisterSimulator(simulant) =>simulator = simulant
    case event: GetCurrentTime =>
      if (running) {
        self.reply(TimeTick(currentTimeTick, currentTimeTick - previousTimeTick))
      } else {
        self.reply(ClockNotStarted)
      }
    case TimeTickReady(value) => {
        if (running) {

          if (value == currentTimeTick) {
            previousTimeTick = currentTimeTick
            currentTimeTick += 1

            var timeDiff = System.currentTimeMillis - previousTickInMSeconds
            if (timeDiff < timeTickInMilliSeconds) {
              Thread.sleep(timeTickInMilliSeconds - timeDiff)
            }

            sendTimeTick() //we could put here a small wait before sending it...
          }
        }
    }
    case _ => log.error("Clock got unknown message")
  }
}
