package uk.co.odinconsultants.bitset

import org.junit.runner.RunWith
import org.scalacheck.Gen.choose
import org.scalacheck.Prop.forAll
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class AtomicBitSetSpec extends WordSpec with Matchers {

  val smallInteger = choose(0,1000)

  "bitset" should {
    "indicate when everything is set" in {
      val propSmallInteger = forAll(smallInteger) { n =>
        val toTest = new AtomicBitSet(n)
        for (i <- 0 to n) toTest.set(i.toLong)
        toTest.isEverythingSet
      }
    }
  }
}
