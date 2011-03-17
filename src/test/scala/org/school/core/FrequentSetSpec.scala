package org.school.core

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class FrequentSetSpec extends FlatSpec with ShouldMatchers {

	behavior of "A frequent set"

	it should "initialize correctly" in {
        val transactions = List(Transaction(ItemSet("1")), Transaction(ItemSet("2")))
        val frequents = FrequentSet[String](transactions)

		frequents.length should be (1)
	}

	it should "have the correct set length" in {
        val t1 = List(Transaction(ItemSet("1")), Transaction(ItemSet("2")))
        val t2 = List(Transaction(ItemSet("2"), ItemSet("13")))
        val t3 = List(Transaction(ItemSet("2", "13"), ItemSet("13")),
                     Transaction(ItemSet("2"), ItemSet("13"), ItemSet("14")))

        FrequentSet(t1).length should be (1)
        FrequentSet(t2).length should be (2)
        FrequentSet(t3).length should be (3)
	}
}

