package uk.co.odinconsultants.graph.search

import org.junit.runner.RunWith
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner
import uk.co.odinconsultants.graph.impl.AdjacencyListGraph
import uk.co.odinconsultants.graph.impl.GraphGenerator._

import scala.annotation.tailrec

@RunWith(classOf[JUnitRunner])
trait GraphFixture extends Matchers {

  val (leaders, edges)  = makeAGraphWith(10, stronglyConnectedComponents, and(eachComponentIsARing))
  val graph             = AdjacencyListGraph(edges)

  @tailrec
  final def checkMonotonicallyIncreasing(vertices: Seq[Int], last: Int): Unit = {
    if (vertices.nonEmpty) {
      val next = vertices.head
      last should be < next
      checkMonotonicallyIncreasing(vertices.tail, next)
    }
  }
}
