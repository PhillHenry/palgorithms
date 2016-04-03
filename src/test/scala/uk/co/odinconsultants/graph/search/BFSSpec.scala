package uk.co.odinconsultants.graph.search

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}
import uk.co.odinconsultants.bitset.AtomicBitSet
import uk.co.odinconsultants.graph.impl.AdjacencyListGraph.asString
import uk.co.odinconsultants.graph.impl.GraphGenerator._
import uk.co.odinconsultants.graph.search.BFS.parallelTopologicalSort

import scala.concurrent.ExecutionContext

@RunWith(classOf[JUnitRunner])
class BFSSpec extends WordSpec with Matchers {

  "search" should {
    "hit all components when starting from the root" ignore new GraphFixture {
      val alreadySeen = new AtomicBitSet(graph.numberOfVertices.toInt)
      BFS.search(graph, 1, alreadySeen)
    }
  }

  "Strongly-connected component graph" should {
    "have topological sort corresponding to its leaders" in new GraphFixture {
      implicit val xc     = ExecutionContext.global
      val sortedVertices  = parallelTopologicalSort(graph, 1)

      withClue(asString(graph) + sortedVertices.groupBy(x => x).filter(_._2.size > 1)) {
        sortedVertices.toSet should have size sortedVertices.size
      }

      withClue(asString(graph)) {

        sortedVertices should have size uniqueVertices(edges).size

        val leaderPosition    = leaders.map { leader =>
          sortedVertices.indexOf(leader)
        }

        checkMonotonicallyIncreasing(leaderPosition, -1)
      }
    }
  }

}
