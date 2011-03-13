package org.school.core

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class ItemSetSpec extends FlatSpec with ShouldMatchers {

	behavior of "An itemset"

	it should "initialize with varargs" in {
		val itemset = ItemSet(Item("class1",1,1.0,0.2), Item("class2",1,0.5,0.1))

		itemset.minsup should be (0.1)
		itemset.minconf should be (0.5)
	}

	it should "initialize with a list" in {
		val items = List[Item](Item("class1",1,1.0,0.2), Item("class2",1,0.5,0.1))
		val itemset = ItemSet(items)

		itemset.minsup should be (0.1)
		itemset.minconf should be (0.5)
	}
}

