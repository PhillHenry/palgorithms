package uk.co.odinconsultants.graph.search

import org.scalatest.{Matchers, WordSpec}
import uk.co.odinconsultants.graph.impl.AdjacencyListGraph.asString
import uk.co.odinconsultants.graph.impl.{VertexId, AdjacencyListGraph}
import uk.co.odinconsultants.graph.impl.GraphGenerator._

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext

class BFSSpec extends WordSpec with Matchers {

  "Strongly-connected component graph" should {
    "have topological sort corresponding to its leaders" in {
      val (leaders, edges)  = makeAGraphWith(10, stronglyConnectedComponents, and(eachComponentIsARing))
      val graph             = AdjacencyListGraph(edges)

      implicit val xc = ExecutionContext.global
      val sorted            = BFS.topologicalSort(graph, 1)

      withClue(asString(graph) + sorted.groupBy(x => x).filter(_._2.size > 1)) {
        sorted.toSet should have size sorted.size
      }

      withClue(asString(graph)) {

        sorted should have size uniqueVertices(edges).size

        val leaderPosition    = leaders.map { leader =>
          sorted.indexOf(leader)
        }

        @tailrec
        def checkMonotonicallyIncreasing(vertices: Seq[Int], last: Int): Unit = {
          if (vertices.nonEmpty) {
            val next = vertices.head
            last should be < next
            checkMonotonicallyIncreasing(vertices.tail, next)
          }
        }

        checkMonotonicallyIncreasing(leaderPosition, -1)
      }
    }
  }

}
