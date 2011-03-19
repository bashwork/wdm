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
		val left  = Transaction(ItemSet((1 to 5).toList), ItemSet(7, 8), ItemSet(9))
		val pass1 = Transaction(ItemSet(2, 3), ItemSet(7), ItemSet(9))
		val pass2 = Transaction(ItemSet(7), ItemSet(9))
		val pass3 = Transaction(ItemSet(2))
		val pass4 = Transaction(ItemSet(2, 3), ItemSet(9))
		val fail1 = Transaction[Int]()
		val fail2 = Transaction(ItemSet(2), ItemSet(3), ItemSet(9))
		val fail3 = Transaction(ItemSet(2), ItemSet(8), ItemSet(9), ItemSet(10))

		left contains pass1 should be (true)
		left contains pass2 should be (true)
		left contains pass3 should be (true)
		left contains pass4 should be (true)
		left contains fail2 should be (false)
		left contains fail3 should be (false)
		left contains fail1 should be (false)
		fail1 contains left should be (false)
	}

	it should "check if another transaction is equal" in {
		val left  = Transaction(ItemSet(2, 3), ItemSet(7), ItemSet(9))
		val right = Transaction(ItemSet(2, 3), ItemSet(7), ItemSet(9))
		val fail  = Transaction(ItemSet(2, 3), ItemSet(9))

		left == right should be (true)
		left == fail should  be (false)
	}

	it should "convert to a string correctly" in {
		val instance = Transaction(ItemSet(2, 3), ItemSet(7), ItemSet(9))
		val expected = "<{2,3}{7}{9}>"

		instance.toString should be (expected)
	}

	it should "generate all subsequences correctly" in {
		val instance = Transaction(ItemSet(2, 3), ItemSet(7), ItemSet(9))
		val actual   = instance.subsequences
		val expected = List(
			Transaction(ItemSet(3), ItemSet(7), ItemSet(9)),
			Transaction(ItemSet(2), ItemSet(7), ItemSet(9)),
			Transaction(ItemSet(2, 3), ItemSet(9)),
			Transaction(ItemSet(2, 3), ItemSet(7)))

		expected.size should be (actual.size)
		for (index <- 0 to expected.size - 1) {
			expected(index) == actual(index) should be (true)
		}
	}

	it should "join another transaction correctly" in {
		val left1  = Transaction(ItemSet(1), ItemSet(2), ItemSet(4))
		val left2  = Transaction(ItemSet(1, 2), ItemSet(4))
		val right1 = Transaction(ItemSet(2), ItemSet(4, 5))
		val right2 = Transaction(ItemSet(3), ItemSet(4, 5))
		val Some(actual1) = left1 join right1
		val Some(actual2) = left2 join right1
		val actual3 = left2 join right2
		val expected1 = Transaction(ItemSet(1), ItemSet(2), ItemSet(4, 5))
		val expected2 = Transaction(ItemSet(1, 2), ItemSet(4, 5))

		expected1 == actual1 should be (true)
		expected2 == actual2 should be (true)
		actual3 should be (None)
	}
}

