package com.botball.environment

case class ScannerData(nodes: List[Node]) extends SensorData

trait Scanning extends Sensing {this: Node =>
  override abstract def sense: List[SensorData] = ScannerData(seenNodes) :: super.sense
  def seenNodes: List[Node] = {
    scene.nodes.filterNot( node => { node == this } )
  }
  def scene: Scene = error("Not Implemented")
}
