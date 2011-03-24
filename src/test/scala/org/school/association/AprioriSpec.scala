package org.school.association

import org.scalatest.{FlatSpec,PrivateMethodTester}
import org.scalatest.matchers.ShouldMatchers
import org.school.core.loader.MemoryLoader
import org.school.core.format.SequentialFormat
import org.school.core.support.SingleSupport
import org.school.core.{ItemSet, Transaction, FrequentSet}

class AprioriSpec extends FlatSpec
	with ShouldMatchers with PrivateMethodTester {

        val dataset = """
<{1, 3, 4}> 
<{2, 3, 5}> 
<{1, 2, 3, 5}> 
<{2, 5}>"""

	behavior of "the apriori algorithm"

	it should "initialize correctly" in {
		val s  = List(Transaction((1 to 5).map { x => ItemSet(x.toString) }.toList))
		val ms = SingleSupport[String](0.1)
		val apriori = new Apriori(s, ms)

		apriori should not be (null)
	}

	it should "produce f1 correctly" in {
		val initialize = PrivateMethod[FrequentSet[String]]('initialize)
		val s  = List(
            Transaction(ItemSet("3","1")),
            Transaction(ItemSet("4", "3")),
            Transaction(ItemSet("1", "3")))
		val ms = SingleSupport[String](0.4)
		val apriori = new Apriori(s, ms)
		val f1 = (apriori invokePrivate initialize()).unique

		f1 should contain ("1")		// support 2/3 >= 0.4
		f1 should not contain ("4")	// support 1/3 <  0.4
		f1 should contain ("3")		// support 3/3 >= 0.4
	}

    it should "produce a dataset correctly" in {
        val loader   = MemoryLoader(dataset)
        val database = SequentialFormat.process(loader)
		val support  = SingleSupport[String](0.3)
		val apriori  = new Apriori(database, support)
        val actual   = apriori.process()

        val f1 = FrequentSet(
            Transaction(ItemSet("1")),
            Transaction(ItemSet("2")),
            Transaction(ItemSet("3")),
            Transaction(ItemSet("5")))
        val f2 = FrequentSet(
            Transaction(ItemSet("1","3")),
            Transaction(ItemSet("2","3")),
            Transaction(ItemSet("2","5")),
            Transaction(ItemSet("3","5")))
        val f3 = FrequentSet(Transaction(ItemSet("2","3","5")))
        val expected = List(f1, f2, f3)

		actual.size should be (expected.size)
		for (index <- 0 to actual.size - 1) {
            actual(index).transactions ==
                expected(index).transactions should be (true) 
        }

    }
}

