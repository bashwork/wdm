package org.school.association

import org.scalatest.{FlatSpec,PrivateMethodTester}
import org.scalatest.matchers.ShouldMatchers
import org.school.core.{MultipleSupport, SingleSupport}
import org.school.core.{ItemSet, Transaction, FrequentSet}
import scala.tools.nsc.Interpreter._

class PrefixSpanSpec extends FlatSpec
	with ShouldMatchers with PrivateMethodTester {

	behavior of "the PrefixSpan algorithm"

	it should "initialize correctly" in {
		val s   = (1 to 5).map { x => Transaction(ItemSet(x.toString)) }.toList
		val ms  = MultipleSupport((1 to 5).map { x => (x.toString, x*0.1) } .toMap)
		val prefix = new PrefixSpan(s, ms)

		prefix should not be (null)
	}

	it should "produce f1 correctly" in {
		val initialize = PrivateMethod[FrequentSet[String]]('initialize)
		val s   = List(Transaction(ItemSet("3")), Transaction(ItemSet("4", "3")), Transaction(ItemSet("1", "3")))
		val ms  = MultipleSupport((1 to 5).map { x => (x.toString, x*0.1) } .toMap)
		val prefix = new PrefixSpan(s, ms)
		val f1  = (prefix invokePrivate initialize()).unique

		f1 should contain ("1")		// support 1/3 >= 0.1
		f1 should not contain ("4")	// support 1/3 <  0.4
		f1 should contain ("3")		// support 3/3 >= 0.3
	}

	it should "process a dataset correctly" in {
		val sequence = List(
            Transaction(ItemSet("30"), ItemSet("90")),
            Transaction(ItemSet("10", "20"), ItemSet("30"), ItemSet("10", "40", "60", "70")),
            Transaction(ItemSet("30", "50", "70", "80")),
            Transaction(ItemSet("30"), ItemSet("30", "40", "70", "80"), ItemSet("90")),
            Transaction(ItemSet("90")))
		val prefix = new PrefixSpan(sequence, SingleSupport[String](0.25))

        val f1 = FrequentSet(List("30", "40", "70", "80", "90").map { x=>
            Transaction(ItemSet(x)) }.toList)
        val f2 = FrequentSet(
            Transaction(ItemSet("30"), ItemSet("40")),
            Transaction(ItemSet("30"), ItemSet("70")),
            Transaction(ItemSet("30"), ItemSet("90")),
            Transaction(ItemSet("30", "70")),
            Transaction(ItemSet("30", "80")),
            Transaction(ItemSet("40", "70")),
            Transaction(ItemSet("70", "80")))
        val f3 = FrequentSet(Transaction(ItemSet("30"), ItemSet("40", "70")),
            Transaction(ItemSet("30", "40", "70")))

		val expecteds = List(f1, f2, f3)
		val actual = prefix.process

        expecteds.zipWithIndex foreach { case(expected, index) =>
            expected == actual(index) should be (true) }
	}
}

