package com.botball.environment

import akka.actor._

class Robot extends Actor {
  def receive = {
    case event: TimeTick => log.debug("Robot got time tick")
    case event: List[SensorData] => log.debug("Robot got sensor data")
    case _ => log.error("Robot got unknown message")
  }
}