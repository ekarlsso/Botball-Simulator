package com.botball.math


class Vec2(xvalue: Double, yvalue: Double) {

  private val values: List[Double] = List(xvalue, yvalue)

  def this(values: List[Double]) = this(values(0), values(1))

  def x = values(0)

  def y = values(1)

  def multiply(value: Double): Vec2 = Vec2(values.map(v => value * v))

  def add(vec: Vec2): Vec2 = Vec2(vec.x + x, vec.y + y)

  def sub(vec: Vec2): Vec2 = Vec2(x - vec.x, y - vec.y)

  def apply(index: Int): Double = values(index)

  def norm: Double = math.sqrt(x*x + y*y)

  def rotate(radians: Double): Vec2 =
    Vec2(math.cos(radians) * norm, math.sin(radians) * norm)

  def toList: List[Double] = values
}

object Vec2 {

  def apply(x: Double, y: Double): Vec2 = new Vec2(x, y)

  def apply(list: List[Double]): Vec2 = new Vec2(list)

}