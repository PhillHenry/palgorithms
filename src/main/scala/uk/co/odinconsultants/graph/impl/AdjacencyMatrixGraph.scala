package uk.co.odinconsultants.graph.impl

import uk.co.odinconsultants.graph.Graph

import scala.collection.immutable.Seq

class AdjacencyMatrixGraph[@specialized(Int, Long)V](private [AdjacencyMatrixGraph] val adjacencyMatrix: Array[Array[VertexId]])
extends Graph {
  override def neighboursOf(vertexId: VertexId): Seq[VertexId] = adjacencyMatrix(vertexId.toInt).filter(_ != 0).to

  override def numberOfVertices: VertexId = adjacencyMatrix.length
}

object AdjacencyMatrixGraph {
  def apply(edges: Seq[Edge], size: Int): AdjacencyListGraph = ???

  def adjacencyMatrixFrom(edges: Seq[Edge], size: Int): Array[Array[VertexId]] = {
    val matrix = Array.ofDim[VertexId](size, size)
    edges.foreach { edge =>
      matrix(edge._1.toInt)(edge._2.toInt) = 1
    }
    matrix
  }
}
