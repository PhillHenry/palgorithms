package uk.co.odinconsultants.graph.impl

import uk.co.odinconsultants.graph.Graph

import scala.annotation.tailrec

import scala.collection.immutable.Seq

class AdjacencyListGraph(private [AdjacencyListGraph] val adjacencyList: Array[Array[VertexId]]) extends Graph {

  override def neighboursOf(vertexId: VertexId): Seq[VertexId] = adjacencyList(vertexId.toInt).to[Seq]

  override def numberOfVertices: Long = adjacencyList.length.toLong
}

object AdjacencyListGraph {


  def apply(edges: Seq[Edge]): AdjacencyListGraph = {
    new AdjacencyListGraph(adjacencyListFrom(edges))
  }

  def adjacencyListFrom(edges: Seq[Edge]): Array[Array[VertexId]] = {

    type GraphMappings = Map[VertexId, List[VertexId]]

    @tailrec
    def populate(edges: Seq[Edge], graphMappings: GraphMappings): GraphMappings = {
      if (edges.isEmpty) {
        graphMappings
      } else {
        val edge      = edges.head
        val key       = edge._1
        val adjacent  = graphMappings(key)
        populate(edges.tail, graphMappings + (key -> (edge._2 +: adjacent)))
      }

    }

    val mappings  = Map[VertexId, List[VertexId]]() withDefault { x => List[VertexId]() }
    val populated = populate(edges, mappings)
    val arraySize = populated.keys.max
    val asArray   = Array.ofDim[Array[VertexId]](arraySize.toInt + 1) // TODO - Longs, no?
    populated foreach { case (k, v) =>
      asArray(k.toInt) = v.toArray
    }
    asArray
  }

  def asString(graph: AdjacencyListGraph): String = asString(graph.adjacencyList)

  def asString(adjacencyList: Array[Array[VertexId]]): String =
    adjacencyList.zipWithIndex.map(x => x._2 + " -> " + (if (x._1 == null) "null" else "[" + x._1.mkString(", ") + "]")).mkString("\n") + "\n"

}
