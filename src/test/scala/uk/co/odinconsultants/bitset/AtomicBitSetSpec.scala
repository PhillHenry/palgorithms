package uk.co.odinconsultants.bitset

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{WordSpec, Matchers}
//import org.scalatest.prop.GeneratorDrivenPropertyChecks

@RunWith(classOf[JUnitRunner])
class AtomicBitSetSpec extends WordSpec /*with GeneratorDrivenPropertyChecks*/ with Matchers {
  "bitset" should {
    "indicate when everything is set" in {
//      forAll { x: Int =>
//        val toTest = new AtomicBitSet(x)
//        for (i <- (0 to x)) toTest.set(i.toLong)
//        toTest.isEverythingSet() shouldBe true
//      }
    }
  }
}
