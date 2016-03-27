package uk.co.odinconsultants.graph.impl

import org.scalatest.{Matchers, WordSpec}

class AdjacencyListGraphSpec extends WordSpec with Matchers {

  import GraphGenerator._
  import AdjacencyListGraph._

  "graph represented as an adjacency list" should {
    "obey laws of graphs" in {
      val (leaders, edges)  = makeAGraphWith(10, stronglyConnectedComponents, and(eachComponentIsARing))
      val adjacencyList     = mappings(edges)
      withClue(asString(adjacencyList)) {
        adjacencyList should have size uniqueVertices(edges).size
        checkConnectedGraphFor(adjacencyList)
      }
    }
  }

  def checkConnectedGraphFor(adjacencyList: Array[Array[VertexId]]): Unit =
    adjacencyList foreach { associated =>
      associated.length should be > 0
    }
}
