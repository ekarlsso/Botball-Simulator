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

  def advanceSimulation(timeTick:Long) {
    nodes = nodes.map(node => {
      node.applyControllers(timeTick - previousTimeTick, timeTick)
      node
    })

    previousTimeTick = timeTick
  }
}
