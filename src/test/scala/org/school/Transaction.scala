package org.school.core

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class TransactionSpec extends FlatSpec with ShouldMatchers {

	behavior of "A transaction"

	it should "initialize with varargs" in {
		val itemset = ItemSet(Item("class1",1,1.0,0.2), Item("class2",1,0.5,0.1))
		val transaction = Transaction(itemset, itemset)

		transaction.sets.length should be (2)
	}

	it should "initialize with a list" in {
		val itemset = ItemSet(Item("class1",1,1.0,0.2), Item("class2",1,0.5,0.1))
		val itemsets = List[ItemSet](itemset, itemset)
		val transaction = Transaction(itemsets)

		transaction.sets.length should be (2)
	}

	it should "produce all unique candidates" in {
        var items1 = ('a' to 'm').map {x => Item(x.toString) }.toList
        var items2 = ('g' to 'z').map {x => Item(x.toString) }.toList
		val transaction = Transaction(ItemSet(items1), ItemSet(items2))
		val candidates = transaction.candidates

		candidates.size should be (26)
		candidates(Item("a")) should be (1)
		candidates(Item("g")) should be (2)
		candidates(Item("z")) should be (1)
	}
}

