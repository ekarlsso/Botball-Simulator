package com.botball.environment

/**
 * Represents the simulation scene that contains the simulated nodes.
 */
class Scene {

  private var nodes:List[Evaluable] = List()

  def registerNode(node: Evaluable): List[Evaluable] = {
    nodes = node :: nodes
    nodes
  }

  def unRegisterNode(node:Node): List[Node] = {
    nodes = nodes.filterNot(n => n == node)
    nodes
  }

  def advanceSimulation(timeDiff:Long, timeTick:Long) {
      nodes = nodes.map(node => {
      node.applyControllers(timeDiff, timeTick)
      node
    })
  }

  
}
