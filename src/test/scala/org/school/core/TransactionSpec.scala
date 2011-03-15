package org.school.core

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class TransactionSpec extends FlatSpec with ShouldMatchers {

	behavior of "A transaction"

	it should "initialize with varargs" in {
		val itemset = ItemSet[String]("class1", "class2")
		val transaction = Transaction[String](itemset, itemset)

		transaction.sets.length should be (2)
	}

	it should "initialize with a list" in {
		val itemset = ItemSet[String]("class1","class2")
		val itemsets = List[ItemSet[String]](itemset, itemset)
		val transaction = Transaction[String](itemsets)

		transaction.sets.length should be (2)
	}

	it should "be able to produce all unique candidates" in {
        var items1 = ('a' to 'm').map { _.toString }.toList
        var items2 = ('g' to 'z').map { _.toString }.toList
		val transaction = Transaction(ItemSet(items1), ItemSet(items2))
		val candidates = transaction.allItems.toSet

		candidates.size should be (26)
	}

	it should "be able to produce the correct minsup" in {
        var items  = ('a' to 'm').map { _.toString }.toList
		val values = items.map { x => (x, (x(0) - '`') * 0.1) }
		val support = MultipleSupport(values.toMap)
		val transaction = Transaction(ItemSet(items.take(10)), ItemSet(items.takeRight(10)))

		transaction.minsup(support) should be (0.1)
	}

	it should "give the correct size and length defined in WDM" in {
        var items1 = ('a' to 'd').map { _.toString }.toList
        var items2 = ('e' to 'g').map { _.toString }.toList
		val transaction = Transaction(ItemSet(items1), ItemSet(items2))

		transaction.size   should be (2)
		transaction.length should be (7)
	}

	it should "check if another transaction is a subset" in {
		val left  = Transaction(ItemSet((1 to 5).toList), ItemSet(7, 8),ItemSet(9))
		val right = Transaction(ItemSet(2, 3), ItemSet(7), ItemSet(9))
		val fail  = Transaction(ItemSet(2, 3), ItemSet(9))

		left contains right should be (true)
		left contains fail  should be (false)
	}
}

