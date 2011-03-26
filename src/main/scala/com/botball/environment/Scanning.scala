package com.botball.environment

case class ScannerData(nodes: List[Node]) extends SensorData

trait Scanning extends Sensing {
  this: Node =>
  override abstract def sense: List[SensorData] = ScannerData(foundNodes) :: super.sense
  protected def foundNodes: List[Node] = {
    scene.nodes.filterNot( node => { node == this } )
  }
  protected def scene: Scene = error("Not Implemented")
}
