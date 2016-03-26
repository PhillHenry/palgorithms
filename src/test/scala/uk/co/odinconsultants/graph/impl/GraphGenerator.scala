package uk.co.odinconsultants.graph.impl

import java.lang.Math.pow

import scala.collection.immutable.Seq
import scala.collection.mutable.ArrayBuffer

object GraphGenerator {

  type MyVertex = Long

  type MyEdge[T] = (MyVertex, T, MyVertex)

  type ComponentFn[T] = Seq[MyVertex] => Seq[MyEdge[T]]

  def and[T](t: T): T = identity(t)

  def componentsConnectedBy[T](t: T): ComponentFn[T] = { vertices =>
    vertices.zip(vertices.drop(1)).map{ case(from, to) =>
      val edge: MyEdge[T] = (from, t,to)
      edge
    }
  }

  def toUniqueVertexIDs[T](edges: Seq[MyEdge[T]]): Set[MyVertex] = {
    val vertices = new ArrayBuffer[MyVertex]()
    edges foreach { edge =>
      vertices += edge._1
      vertices += edge._3
    }
    vertices.toSet
  }

  def makeAGraphWith[T](n: Int, intraComponentFn: ComponentFn[T], interComponentFn: ComponentFn[T]): (Seq[MyVertex], Seq[MyEdge[T]]) = {
    val leaders = (2 to n + 1).map(pow(_, 2).toLong)
    val edges   = intraComponentFn(leaders) ++ interComponentFn(leaders)
    (leaders, edges)
  }

  def eachComponentIsARingWithVerticesConnectedBy[T](t: T): ComponentFn[T] = { leaders =>
    val edges = new ArrayBuffer[MyEdge[T]]()
    var last  = 1L
    for (leader <- leaders) {
      edges += ((leader, t, last))
      (last + 1 to leader).foreach { vertexId =>
        val edge = (last, t, vertexId)
        edges += edge
        last = vertexId
      }
      last = leader +1
    }
    edges.to
  }

}
