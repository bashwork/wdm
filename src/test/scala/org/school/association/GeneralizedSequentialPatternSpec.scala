package org.school.association

import org.scalatest.{FlatSpec,PrivateMethodTester}
import org.scalatest.matchers.ShouldMatchers
import org.school.core.support.{MultipleSupport, SingleSupport}
import org.school.core.{ItemSet, Transaction, FrequentSet}

class GeneralizedSequentialPatternSpec extends FlatSpec
	with ShouldMatchers with PrivateMethodTester {

	behavior of "the GeneralizedSequentialPattern algorithm"

	it should "initialize correctly" in {
		val s   = (1 to 5).map { x => Transaction(ItemSet(x.toString)) }.toList
		val ms  = MultipleSupport((1 to 5).map { x => (x.toString, x*0.1) } .toMap)
		val gsp = new GeneralizedSequentialPattern(s, ms)

		gsp should not be (null)
	}

	it should "produce f1 correctly" in {
		val initialize = PrivateMethod[FrequentSet[String]]('initialize)
		val s   = List(Transaction(ItemSet("3")), Transaction(ItemSet("4", "3")), Transaction(ItemSet("1", "3")))
		val ms  = MultipleSupport((1 to 5).map { x => (x.toString, x*0.1) } .toMap)
		val gsp = new GeneralizedSequentialPattern(s, ms)
		val f1  = (gsp invokePrivate initialize()).unique

		f1 should contain ("1")		// support 1/3 >= 0.1
		f1 should not contain ("4")	// support 1/3 <  0.4
		f1 should contain ("3")		// support 3/3 >= 0.3
	}

	it should "calculate sdc correctly" in {
		val evaluateSdc = PrivateMethod[Boolean]('evaluateSdc)
		val s   = (1 to 5).map { x => Transaction(ItemSet(x.toString)) }.toList
		val ms  = MultipleSupport((1 to 5).map { x => (x.toString, x*0.1) } .toMap)
		ms.sdc = 0.4 // artificial support difference constraint
		val gsp = new GeneralizedSequentialPattern(s, ms)

		(gsp invokePrivate evaluateSdc(s(0), s(1))) should be (false)
		(gsp invokePrivate evaluateSdc(s(0), s(4))) should be (true)
	}

	it should "generate candidate2 correctly" in {
		val candidateGen2 = PrivateMethod[List[Transaction[String]]]('candidateGen2)
		val sequence = (1 to 5).map { x => Transaction(ItemSet(x.toString)) }.toList
		val support = MultipleSupport((1 to 5).map { x => (x.toString, x*0.1) } .toMap)
		val gsp = new GeneralizedSequentialPattern(sequence, support)

		val frequent = FrequentSet((1 to 3).map { x =>
            Transaction(List(ItemSet(x.toString)), 2) }.toList)
		val expected = List(
			Transaction(ItemSet("1", "2")), Transaction(ItemSet("1"), ItemSet("2")),
			Transaction(ItemSet("1", "3")), Transaction(ItemSet("1"), ItemSet("3")),
			Transaction(ItemSet("2", "3")), Transaction(ItemSet("2"), ItemSet("3")))
		val actual = (gsp invokePrivate candidateGen2(frequent))

		actual.size should be (expected.size)
		for (index <- 0 to actual.size - 1) {
            actual(index) == expected(index) should be (true) 
		}
	}

	it should "generate frequent2 correctly" in {
		val buildFrequent = PrivateMethod[FrequentSet[String]]('buildFrequent)
		val sequence = List(
            Transaction(ItemSet("1", "2")), Transaction(ItemSet("1"), ItemSet("2")),
            Transaction(ItemSet("1", "2")), Transaction(ItemSet("1"), ItemSet("2")),
            Transaction(ItemSet("1"), ItemSet("3"))) // 1(5/5), 2(4/5), 3(1/5)
		val gsp = new GeneralizedSequentialPattern(sequence, SingleSupport[String](0.3))

		val candidate = List(
			Transaction(ItemSet("1", "2")), Transaction(ItemSet("1"), ItemSet("2")),
			Transaction(ItemSet("1", "3")), Transaction(ItemSet("1"), ItemSet("3")),
			Transaction(ItemSet("2", "3")), Transaction(ItemSet("2"), ItemSet("3")))
		val expecteds = List(
                Transaction(ItemSet("1", "2")),
                Transaction(ItemSet("1"), ItemSet("2")))
		val actual = (gsp invokePrivate buildFrequent(candidate))

		actual.transactions.size should be (expecteds.size)
        expecteds.zipWithIndex foreach { case(expected, index) =>
            expected == actual.transactions(index) should be (true) }
		actual.transactions(0).length should be(2)
		actual.transactions(1).length should be(2)
	}

	it should "generate candidateN correctly" in {
		val candidateGenN = PrivateMethod[List[Transaction[String]]]('candidateGenN)
		val sequence = (1 to 5).map { x => Transaction(ItemSet(x.toString)) }.toList
		val gsp = new GeneralizedSequentialPattern(sequence, SingleSupport[String](0.1))

		val frequent = FrequentSet(
			Transaction(ItemSet("1", "2"), ItemSet("4")),
			Transaction(ItemSet("1", "2"), ItemSet("5")),
			Transaction(ItemSet("1"), ItemSet("4", "5")),
			Transaction(ItemSet("1", "4"), ItemSet("6")),
			Transaction(ItemSet("2"), ItemSet("4", "5")),
			Transaction(ItemSet("2"), ItemSet("4"), ItemSet("6")))
		val expected = List(Transaction(ItemSet("1", "2"), ItemSet("4", "5")))
		val actual = (gsp invokePrivate candidateGenN(frequent))

		actual.size should be (expected.size)
		for (index <- 0 to actual.size - 1) {
            actual(index) == expected(index) should be (true) 
		}
	}

	it should "generate frequentN correctly" in {
		val candidateGenN = PrivateMethod[List[Transaction[String]]]('candidateGenN)
		val buildFrequent = PrivateMethod[FrequentSet[String]]('buildFrequent)
		val sequence = List(
            Transaction(ItemSet("30"), ItemSet("90")),
            Transaction(ItemSet("10", "20"), ItemSet("30"), ItemSet("10", "40", "60", "70")),
            Transaction(ItemSet("30", "50", "70", "80")),
            Transaction(ItemSet("30"), ItemSet("30", "40", "70", "80"), ItemSet("90")),
            Transaction(ItemSet("90")))
		val gsp = new GeneralizedSequentialPattern(sequence, SingleSupport[String](0.25))

        val frequent = FrequentSet(
            Transaction(ItemSet("30"), ItemSet("40")),
            Transaction(ItemSet("30"), ItemSet("70")),
            Transaction(ItemSet("30"), ItemSet("90")),
            Transaction(ItemSet("30", "70")),
            Transaction(ItemSet("30", "80")),
            Transaction(ItemSet("40", "70")),
            Transaction(ItemSet("70", "80")))
        val expected = FrequentSet(
            Transaction(ItemSet("30"), ItemSet("40", "70")),
            Transaction(ItemSet("30", "70", "80")))
		val candidates = (gsp invokePrivate candidateGenN(frequent))
		val actual = (gsp invokePrivate buildFrequent(candidates))

		actual.size should be (expected.size)
		for (index <- 0 to actual.size - 1) {
            actual.transactions(index) == expected.transactions(index) should be (true) 
		}
	}

	it should "process a dataset correctly" in {
		val sequence = List(
            Transaction(ItemSet("30"), ItemSet("90")),
            Transaction(ItemSet("10", "20"), ItemSet("30"), ItemSet("10", "40", "60", "70")),
            Transaction(ItemSet("30", "50", "70", "80")),
            Transaction(ItemSet("30"), ItemSet("30", "40", "70", "80"), ItemSet("90")),
            Transaction(ItemSet("90")))
		val gsp = new GeneralizedSequentialPattern(sequence, SingleSupport[String](0.25))

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

		val expected = List(f1, f2, f3)
		val actual = gsp.process

		// thar be dragons here
		actual.size should be (expected.size)
		for (index <- 0 to actual.size - 1) {
			val (left, right) = (actual(index).transactions, expected(index).transactions)
			left.size should be (right.size)
			for (j <- 0 to left.size - 1) {
            	left(j) == right(j) should be (true) 
			}
		}
	}
}

