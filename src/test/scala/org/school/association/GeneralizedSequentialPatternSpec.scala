package org.school.association

import org.scalatest.{FlatSpec,PrivateMethodTester}
import org.scalatest.matchers.ShouldMatchers
import org.school.core.{MultipleSupport}
import org.school.core.{ItemSet, Transaction, FrequentSet}
import scala.tools.nsc.Interpreter._

class GeneralizedSequentialPatternSpec extends FlatSpec
	with ShouldMatchers with PrivateMethodTester {

	behavior of "the GeneralizedSequentialPattern"

	it should "initialize correctly" in {
		val s   = (1 to 5).map { x => Transaction(ItemSet(x.toString)) }.toList
		val ms  = MultipleSupport((0 to 5).map { x => (x.toString, x*0.1) } .toMap)
		val gsp = new GeneralizedSequentialPattern(s, ms)

		gsp should not be (null)
	}

	it should "produce f1 correctly" in {
		val initialize = PrivateMethod[FrequentSet[String]]('initialize)
		val s   = List(Transaction(ItemSet("3")), Transaction(ItemSet("4", "3")), Transaction(ItemSet("1", "3")))
		val ms  = MultipleSupport((0 to 5).map { x => (x.toString, x*0.1) } .toMap)
		val gsp = new GeneralizedSequentialPattern(s, ms)
		val f1  = (gsp invokePrivate initialize()).unique

		f1 should contain ("1")		// support 1/3 >= 0.1
		f1 should not contain ("4")	// support 1/3 <  0.4
		f1 should contain ("3")		// support 3/3 >= 0.3
	}

	it should "calculate sdc correctly" in {
		val evaluateSdc = PrivateMethod[Boolean]('evaluateSdc)
		val s   = (1 to 5).map { x => Transaction(ItemSet(x.toString)) }.toList
		val ms  = MultipleSupport((0 to 5).map { x => (x.toString, x*0.1) } .toMap)
		ms.sdc = 0.4 // artificial support difference constraint
		val gsp = new GeneralizedSequentialPattern(s, ms)

		(gsp invokePrivate evaluateSdc(s(0), s(1))) should be (false)
		(gsp invokePrivate evaluateSdc(s(0), s(4))) should be (true)
	}

	it should "generate candidate2 correctly" in {
		val candidateGen2 = PrivateMethod[List[Transaction[String]]]('candidateGen2)
		val s   = (1 to 5).map { x => Transaction(ItemSet(x.toString)) }.toList
		val ms  = MultipleSupport((0 to 5).map { x => (x.toString, x*0.1) } .toMap)
		val gsp = new GeneralizedSequentialPattern(s, ms)
		val frequent = FrequentSet((1 to 3).map { x => Transaction(ItemSet(x.toString)) }.toList)
		val actual = (gsp invokePrivate candidateGen2(frequent))
		val expected = List(
			Transaction(ItemSet("1", "2")), Transaction(ItemSet("1"), ItemSet("2")),
			Transaction(ItemSet("1", "3")), Transaction(ItemSet("1"), ItemSet("3")),
			Transaction(ItemSet("2", "3")), Transaction(ItemSet("2"), ItemSet("3")))

		actual.zip(expected) foreach { case(aN, eN) => aN == eN should be (true) }
	}
}

