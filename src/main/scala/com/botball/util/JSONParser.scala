package com.botball.util

import com.botball.environment.Node

object JSONParser {
  def nodesToJSON(robots: List[Node]): String = {
    "{\"robots\":" + robots.map(r=>r.toJSON).mkString("[", ", ", "]") + "}"
  }
}