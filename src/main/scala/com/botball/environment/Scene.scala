package com.botball.environment

/**
 * Represents the simulation scene that contains the simulated nodes.
 */
class Scene {

  private var previousTimeTick:Long = 0
  private var nodes:List[Evaluable] = List()

  def registerNode(node: Evaluable): List[Evaluable] = {
    nodes = node :: nodes
    nodes
  }

  def advanceSimulation(timeTick:Long) {
    nodes = nodes.map(node => {
      node.evaluate(timeTick - previousTimeTick, timeTick)
      node
    })

    previousTimeTick = timeTick
  }
}
