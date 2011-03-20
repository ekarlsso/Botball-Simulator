package com.botball.environment

import org.specs.Specification
import org.specs.mock.Mockito
import scalala.tensor.dense.DenseVector

object ScanningNodeSpecs extends Specification with Mockito {
  class ScanningNode extends Node with Scanning
  "ScanningNode" should {
    "find all nodes except scanning node" in {
      val sceneMock = mock[Scene]
      val scanningNode = testNode(vector(1.0, 0.0), sceneMock)
      val foundNode = testNode(vector(1.0, 1.0), sceneMock)
      sceneMock.nodes returns List(scanningNode, foundNode)
      scanningNode.sense must haveTheSameElementsAs(List(ScannerData(List(foundNode))))
    }
    // TODO: Test scanning that only scans toward scanning direction
  }
  def vector(x: Double, y: Double) = DenseVector(x, y, 0.0)
  def testNode(p: DenseVector[Double], s: Scene) = new ScanningNode {
    override def position = p
    override def scene = s
  }
}
