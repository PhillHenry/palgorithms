package uk.co.odinconsultants.graph

import uk.co.odinconsultants.graph.impl._

trait Graph {

  def neighboursOf(vertexId: VertexId): Seq[VertexId]

  def numberOfVertices: Long

}
