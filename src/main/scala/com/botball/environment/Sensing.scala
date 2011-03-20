package com.botball.environment

abstract class SensorData

abstract class Sensing {
  def sense: List[SensorData]
}

class DefaultSensing extends Sensing {
  def sense: List[SensorData] = Nil
}
