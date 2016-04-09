package uk.co.odinconsultants.graph.search

import java.util.concurrent.TimeUnit.SECONDS

import uk.co.odinconsultants.bitset.{AlreadySeen, AtomicBitSet}
import uk.co.odinconsultants.graph.Graph
import uk.co.odinconsultants.graph.impl.VertexId

import scala.annotation.tailrec
import scala.collection.immutable.Seq
import scala.concurrent.Await.result
import scala.concurrent.Future.sequence
import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future}

object BFS {

  def search(g: Graph, start: VertexId, alreadySeen: (VertexId) => Boolean): Unit = {
    @tailrec
    def bfs(toExplore: Seq[VertexId]): Unit = {
      if (toExplore.nonEmpty) {
        val next = toExplore.head
        if (alreadySeen(next.toLong)) {
          bfs(toExplore.tail ++ g.neighboursOf(next))
        } else {
          bfs(toExplore.tail)
        }
      }
    }
    if (alreadySeen(start)) {
      bfs(g.neighboursOf(start))
    }
  }

  def parallelPath(g: Graph, start: VertexId)(implicit xc: ExecutionContext): Seq[VertexId] = {

    val alreadySeen = AlreadySeen(g.numberOfVertices.toInt)

    @tailrec
    def bfs(toSort: Seq[VertexId], seenSoFar: Seq[VertexId]): Seq[VertexId] = {
      if (toSort.isEmpty) {
        seenSoFar
      } else {
        val toPursue  = toSort.filter(vertexId => alreadySeen(vertexId))
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

    alreadySeen(start)
    bfs(g.neighboursOf(start), Seq(start))
  }

  def path(g: Graph, start: VertexId): Seq[VertexId] = {

    val alreadySeen = AlreadySeen(g.numberOfVertices.toInt)

    @tailrec
    def bfs(toSort: Seq[VertexId], seenSoFar: Seq[VertexId]): Seq[VertexId] = {
      if (toSort.isEmpty) {
        seenSoFar
      } else {
        val toPursue = toSort.filter(vertexId => alreadySeen(vertexId))
        val children = toPursue.flatMap(g.neighboursOf)
        bfs(children, seenSoFar ++ toPursue)
      }
    }

    alreadySeen(start)
    bfs(g.neighboursOf(start), Seq(start))
  }

}
