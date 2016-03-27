package uk.co.odinconsultants.graph.search

import uk.co.odinconsultants.bitset.AtomicBitSet
import uk.co.odinconsultants.graph.Graph
import uk.co.odinconsultants.graph.impl.VertexId

import scala.annotation.tailrec
import scala.collection.immutable.Seq

object BFS {

  def topologicalSort(g: Graph, start: VertexId): Seq[VertexId] = {

    val alreadySeen = new AtomicBitSet(g.numberOfVertices.toInt)

    @tailrec
    def bfs(toSort: Seq[VertexId], seenSoFar: Seq[VertexId]): Seq[VertexId] = {
      if (toSort.isEmpty) {
        seenSoFar
      } else {
        val toPursue = toSort.filter(vertexId => alreadySeen.set(vertexId))
        val children = toPursue.flatMap(g.neighboursOf)
        bfs(children, seenSoFar ++ toPursue)
      }
    }

    alreadySeen.set(start)
    bfs(g.neighboursOf(start), Seq(start))
  }

}
