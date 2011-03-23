package org.school.association

import org.scalatest.{FlatSpec,PrivateMethodTester}
import org.scalatest.matchers.ShouldMatchers
import org.school.core.support.MultipleSupport
import org.school.core.{ItemSet, Transaction, FrequentSet}

class MsAprioriSpecSpec extends FlatSpec
	with ShouldMatchers with PrivateMethodTester {

	behavior of "the ms-apriori algorithm"

	it should "initialize correctly" in {
		val s   = (1 to 5).map { x => Transaction(ItemSet(x.toString)) }.toList
		val ms  = MultipleSupport((1 to 5).map { x => (x.toString, x*0.1) } .toMap)
		val msa = new MsApriori(s, ms)

		msa should not be (null)
	}

	it should "produce f1 correctly" in {
		val initialize = PrivateMethod[FrequentSet[String]]('initialize)
		val s   = List(Transaction(ItemSet("3")), Transaction(ItemSet("4", "3")), Transaction(ItemSet("1", "3")))
		val ms  = MultipleSupport((1 to 5).map { x => (x.toString, x*0.1) } .toMap)
		val msa = new MsApriori(s, ms)
		val f1  = (msa invokePrivate initialize()).unique

		f1 should contain ("1")		// support 1/3 >= 0.1
		f1 should not contain ("4")	// support 1/3 <  0.4
		f1 should contain ("3")		// support 3/3 >= 0.3
	}
}

