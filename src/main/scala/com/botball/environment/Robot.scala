package com.botball.environment

import akka.actor._

class Robot extends Actor {
  def receive = {
    case _ => log.error("Robot got unknown message")
  }
}