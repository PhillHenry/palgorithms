package uk.co.odinconsultants.graph.impl

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class AdjacencyListGraphSpec extends WordSpec with Matchers {

  import GraphGenerator._
  import AdjacencyListGraph._

  "graph represented as an adjacency list" should {
    "obey laws of graphs" in {
      val (_, edges)    = makeAGraphWith(10, stronglyConnectedComponents, and(eachComponentIsARing))
      val adjacencyList = adjacencyListFrom(edges)
      withClue(asString(adjacencyList)) {
        adjacencyList should have size uniqueVertices(edges).size
        checkConnectedGraphFor(adjacencyList)
      }
    }
  }

  def checkConnectedGraphFor(adjacencyList: Array[Array[VertexId]]): Unit =
    adjacencyList foreach { associated =>
      associated.length should be > 0
      associated foreach { vertexId =>
        vertexId should be >= 0L
      }
    }
}
