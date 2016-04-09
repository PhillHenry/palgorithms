package uk.co.odinconsultants.bitset

import uk.co.odinconsultants.graph.impl.VertexId

object AlreadySeen {

  implicit def toScalaFn(bitSet: AtomicBitSet): (VertexId) => Boolean = toFn(bitSet)

  def apply(length: Int): (VertexId) => Boolean = {
    val bitSet = new AtomicBitSet(length)
    toFn(bitSet)
  }

  def toFn(bitSet: AtomicBitSet): (VertexId) => Boolean = {
    val fn: (VertexId) => Boolean = { vertexId =>
      bitSet(vertexId)
    }
    fn
  }
}
