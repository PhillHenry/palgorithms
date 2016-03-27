package uk.co.odinconsultants.graph

import uk.co.odinconsultants.graph.impl._

import scala.collection.immutable.Seq

trait Graph {

  def neighboursOf(vertexId: VertexId): Seq[VertexId]

  def numberOfVertices: Long

}
