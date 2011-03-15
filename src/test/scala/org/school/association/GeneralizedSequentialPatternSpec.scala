package org.school.association

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.school.core.{MultipleSupport}
import org.school.core.{ItemSet, Transaction}

class GeneralizedSequentialPatternSpec extends FlatSpec with ShouldMatchers {

	behavior of "the GeneralizedSequentialPattern"

	it should "initialize correctly" in {
		val s   = (1 to 5).map { x => Transaction(ItemSet(x.toString)) }.toList
		val ms  = MultipleSupport((0 to 5).map { x => (x.toString, x*0.1) } .toMap)
		val gsp = new GeneralizedSequentialPattern(s, ms)

		gsp should not be (null)
	}

	it should "produce f1 correctly" in {
		val s   = List(Transaction(ItemSet("3")), Transaction(ItemSet("4", "3")), Transaction(ItemSet("1", "3")))
		val ms  = MultipleSupport((0 to 5).map { x => (x.toString, x*0.1) } .toMap)
		val gsp = new GeneralizedSequentialPattern(s, ms)
		val f1  = gsp.initialize().unique

		f1 should contain ("1")		// support 1/3 >= 0.1
		f1 should not contain ("4")	// support 1/3 <  0.4
		f1 should contain ("3")		// support 3/3 >= 0.3
	}
}

