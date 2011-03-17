package org.school.core

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class ItemSetSpec extends FlatSpec with ShouldMatchers {

	behavior of "An itemset"

	it should "initialize with varargs" in {
		val itemset = ItemSet[String]("class1", "class2")

		itemset.items.size should be (2)
	}

	it should "initialize with a list" in {
		val items = List[String]("class1","class2")
		val itemset = ItemSet[String](items)

		itemset.items.size should be (2)
	}

	it should "equal an itemset with the same values" in {
		val itemset1 = ItemSet((1 to 10).toList)
		val itemset2 = ItemSet((1 to 10).toList)

		itemset1 should equal (itemset2)
	}

	it should "not be equal to an itemset with different values" in {
		val itemset1 = ItemSet((1 to 10).toList)
		val itemset2 = ItemSet((2 to 10).toList)

		itemset1 should not equal (itemset2)
	}

	it should "deliver the correct minimum support" in {
		val values  = (1 to 10).map { x => (x, x * 0.1) }
		val support = MultipleSupport(values.toMap)
		val itemset = ItemSet((1 to 10).toList)

		itemset.minsup(support) should be (0.1)
	}

	it should "convert to a string correctly" in {

		ItemSet(2).toString should be ("{2}")
		ItemSet(2,3).toString should be ("{2,3}")
	}
}

