package com.botball.environment

/**
 * Represents the simulation scene that contains the simulated nodes.
 */
class Scene {

  private var previousTimeTick:Long = 0
  private var nodes:List[Node] = List()

  def registerNode(node:Node): List[Node] = {
    nodes = node :: nodes
    nodes
  }

  def advanceSimulation(timeDifference:Long, timeTick:Long) {
    nodes.map(node => {
      node.applyControllers(timeDifference, timeTick)
    })
  }
}
