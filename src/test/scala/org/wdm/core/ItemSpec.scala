package org.wdm.core

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class ItemSpec extends FlatSpec with ShouldMatchers {

	behavior of "An item"

	it should "initialize correctly" in {
		val item = Item(22, "age")

		item should not be (null)
	}

	it should "equal an itemset with the same values" in {
		val item1 = Item(22, "age")
		val item2 = Item(22, "age")

		item1 should equal (item2)
	}

	it should "not be equal to an itemset with different values" in {
		val item1 = Item(22, "age")
		val item2 = Item(35, "count")

		item1 should not equal (item2)
	}

	it should "convert to a string correctly" in {
		val item = Item(22, "age")

		item.toString should be ("(age,22)")
	}
}

