package com.botball.environment

/**
 * Represents the simulation scene that contains the simulated nodes.
 */
class Scene {

  private var nodes:List[Node] = List()

  def registerNode(node:Node): List[Node] = {
    nodes = node :: nodes
    nodes
  }

  def advanceSimulation() {
    
  }
}
