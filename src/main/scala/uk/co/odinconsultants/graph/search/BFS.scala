package uk.co.odinconsultants.graph.search

import java.util.concurrent.TimeUnit.SECONDS

import uk.co.odinconsultants.bitset.AtomicBitSet
import uk.co.odinconsultants.graph.Graph
import uk.co.odinconsultants.graph.impl.VertexId

import scala.annotation.tailrec
import scala.collection.immutable.Seq
import scala.concurrent.Await.result
import scala.concurrent.Future.sequence
import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future}

object BFS {

  def search(g: Graph, start: VertexId, alreadySeen: AtomicBitSet): Unit = {
    @tailrec
    def bfs(toExpore: Seq[VertexId]): Unit = {
      if (toExpore.nonEmpty) {
        val next = toExpore.head
        if (!alreadySeen.set(next.toLong)) {
          bfs(toExpore.tail ++ g.neighboursOf(next))
        }
      }
    }
    bfs(g.neighboursOf(start))
  }

  def parallelTopologicalSort(g: Graph, start: VertexId)(implicit xc: ExecutionContext): Seq[VertexId] = {

    val alreadySeen = new AtomicBitSet(g.numberOfVertices.toInt)

    @tailrec
    def bfs(toSort: Seq[VertexId], seenSoFar: Seq[VertexId]): Seq[VertexId] = {
      if (toSort.isEmpty) {
        seenSoFar
      } else {
        val toPursue  = toSort.filter(vertexId => alreadySeen.set(vertexId))
        val order     = seenSoFar ++ toPursue

        val futures   = toPursue.map { newVertexId =>
          Future {
            g.neighboursOf(newVertexId)
          }
        }

        val children  = result(sequence(futures), Duration(1, SECONDS)).flatten
        bfs(children, order)
      }
    }

    alreadySeen.set(start)
    bfs(g.neighboursOf(start), Seq(start))
  }

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
