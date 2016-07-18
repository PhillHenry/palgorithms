package uk.co.odinconsultants.graph.impl

import org.scalatest.{Matchers, WordSpec}
import uk.co.odinconsultants.graph.impl.GraphGenerator._

import AdjacencyMatrixGraph._

class AdjacencyMatrixGraphSpec extends WordSpec with Matchers {

  "matrix from edges" should {
    val (_, edges)        = makeAGraphWith(10, stronglyConnectedComponents, and(eachComponentIsARing))
    val expectedDimension = uniqueVertices(edges).size
    val matrix            = adjacencyMatrixFrom(edges, expectedDimension)

    "have correct dimensions" in {
      matrix should have length expectedDimension
      matrix.foreach( row =>
        row should have length expectedDimension
      )
    }

    "have all the pairings" in {
      edges.foreach { edge =>
        matrix(edge._1.toInt)(edge._2.toInt) shouldEqual 1
      }
    }

    "but 0 for everything else" in {
      matrix.zipWithIndex.foreach { case(row, i) =>
        row.zipWithIndex.foreach { case(element, j) =>
            if (!edges.toSet((i.toLong, j.toLong))) element shouldEqual 0
        }
      }
    }
  }

}
