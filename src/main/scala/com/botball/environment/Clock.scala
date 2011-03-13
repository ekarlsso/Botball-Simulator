package com.botball.environment

import se.scalablesolutions.akka.actor._

case class TimeTick(time:Long)
case class GetCurrentTime

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
  private var simulants:List[Actor] = List()

  def receive = {
    case event: GetCurrentTime =>
      self.reply(TimeTick(currentTime))
    case _ => log.error("Clock got unknown message")
  }

}
