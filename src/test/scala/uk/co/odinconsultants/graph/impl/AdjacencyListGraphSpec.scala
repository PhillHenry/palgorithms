package uk.co.odinconsultants.graph.impl

import org.scalatest.{Matchers, WordSpec}

class AdjacencyListGraphSpec extends WordSpec with Matchers {

  import GraphGenerator._
  import AdjacencyListGraph._

  "graph represented as an adjacency list" should {
    "obey laws of graphs" in {
      val (leaders, edges)  = makeASCCGraphWith(10, componentsConnectedBy("owns"), and(eachComponentIsARingWithVerticesConnectedBy("owns")))
      val adjacencyList     = mappings(edges)
      withClue(asString(adjacencyList)) {
        adjacencyList should have size uniqueVertices(edges).size
        adjacencyList foreach { associated =>
          associated.length should be > 0
        }
      }
    }
  }

}
