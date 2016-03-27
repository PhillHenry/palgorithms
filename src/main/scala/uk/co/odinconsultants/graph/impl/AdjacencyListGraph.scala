package uk.co.odinconsultants.graph.impl

import scala.annotation.tailrec

class AdjacencyListGraph[@specialized(Int, Long)T](adjacencyList: Array[Array[T]]) {



}

object AdjacencyListGraph {


  def apply(edges: Seq[Edge]): AdjacencyListGraph[VertexId] = {
    new AdjacencyListGraph[VertexId](mappings(edges))
  }

  def mappings(edges: Seq[Edge]): Array[Array[VertexId]] = {

    type GraphMappings = Map[VertexId, List[VertexId]]

    @tailrec
    def populate(edges: Seq[Edge], graphMappings: GraphMappings): GraphMappings = {
      if (edges.isEmpty) {
        graphMappings
      } else {
        val edge      = edges.head
        val key       = edge._1
        val adjacent  = graphMappings(key)
        populate(edges.tail, graphMappings + (key -> (adjacent :+ edge._2)))
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

  def asString(adjacencyList: Array[Array[VertexId]]): String =
    adjacencyList.map(x => if (x == null) "null" else "[" + x.mkString(", ") + "]").mkString("\n")

}
