package uk.co.odinconsultants.graph.search

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}
import uk.co.odinconsultants.bitset.AtomicBitSet
import uk.co.odinconsultants.bitset.AlreadySeen._
import uk.co.odinconsultants.graph.impl.AdjacencyListGraph.asString
import uk.co.odinconsultants.graph.impl.GraphGenerator._
import uk.co.odinconsultants.graph.search.BFS.{search, parallelPath}

import scala.concurrent.ExecutionContext

@RunWith(classOf[JUnitRunner])
class BFSSpec extends WordSpec with Matchers {

  "search" should {
    "hit all components when starting from the root" in new GraphFixture {
      val numVertices = graph.numberOfVertices.toInt
      val alreadySeen = new AtomicBitSet(numVertices)

      search(graph, leaders.head, alreadySeen)

      withClue((0 to numVertices).filterNot(alreadySeen.get(_)).mkString(", ")) {
        alreadySeen.isEverythingSet shouldBe true
      }
    }
  }

  "Strongly-connected component graph" should {
    "have topological sort corresponding to its leaders" in new GraphFixture {
      implicit val xc     = ExecutionContext.global
      val sortedVertices  = parallelPath(graph, 1)

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
