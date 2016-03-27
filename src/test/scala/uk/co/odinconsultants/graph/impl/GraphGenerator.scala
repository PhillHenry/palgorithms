package uk.co.odinconsultants.graph.impl

import java.lang.Math.pow

import scala.collection.immutable.Seq
import scala.collection.mutable.ArrayBuffer

object GraphGenerator {

  type ComponentFn = Seq[VertexId] => Seq[Edge]

  def and[T](t: T): T = identity(t)

  def stronglyConnectedComponents: ComponentFn = { vertices =>
    vertices.zip(vertices.drop(1)).map{ case(from, to) =>
      val edge: Edge = (from, to)
      edge
    }
  }

  def toUniqueVertexIdIDs[T](edges: Seq[Edge]): Set[VertexId] = {
    val vertices = new ArrayBuffer[VertexId]()
    edges foreach { edge =>
      vertices += edge._1
      vertices += edge._2
    }
    vertices.toSet
  }

  def makeAGraphWith[T](n: Int, intraComponentFn: ComponentFn, interComponentFn: ComponentFn): (Seq[VertexId], Seq[Edge]) = {
    val leaders = (2 to n + 1).map(pow(_, 2).toLong)
    val edges   = intraComponentFn(leaders) ++ interComponentFn(leaders)
    (leaders, edges)
  }

  def eachComponentIsARing: ComponentFn = { leaders =>
    val edges = new ArrayBuffer[Edge]()
    var last  = 0L
    for (leader <- leaders) {
      edges += ((leader, last))
      (last + 1 to leader).foreach { VertexIdId =>
        val edge = (last, VertexIdId)
        edges += edge
        last = VertexIdId
      }
      last = leader + 1
    }
    edges.to
  }

  def uniqueVertices(edges: Seq[Edge]): Set[VertexId] = edges.flatMap(edge => Seq(edge._1, edge._2)).toSet

}
