package com.botball.math

import org.specs._
import org.specs.runner.JUnit4
import org.specs.mock.Mockito
import org.mockito.Matchers._

class Vec2SpecsAsTests extends JUnit4(Vec2Specs)

object Vec2Specs extends Specification {

  "Vec2" should {
    "multiply its values" in {

      val vec = Vec2(10.0, 20.0)

      val vec2 = vec.multiply(2.0)

      vec2.x must_== 20.0
      vec2.y must_== 40.0

      val vec3 = vec.multiply(-4.0)
      vec3(0) must_== -40.0
      vec3(1) must_== -80.0
    }

    "add itself with another vec2" in {

      val vec = Vec2(10.0, 20.0)
      val vec2 = Vec2(30.0, 40.0)

      val vec3 = vec.add(vec2)

      vec3.x must_== 40.0
      vec3.y must_== 60.0
    }

    "sub itself with another vec2" in {

      val vec = Vec2(10.0, 20.0)
      val vec2 = Vec2(30.0, 50.0)

      val vec3 = vec.sub(vec2)

      vec3.x must_== -20.0
      vec3.y must_== -30.0
    }

    "rotate vector" in {
      val vec = Vec2(1.0, 0.0).rotate((90.0).toRadians)
      vec.x must beCloseTo(0.0, 0.0001)
      vec.y must beCloseTo(1.0, 0.0001)
    }
  }
}