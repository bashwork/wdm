package org.school.core

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class ItemSpec extends FlatSpec with ShouldMatchers {

	behavior of "An item"

	it should "initialize with default values" in {
		val item = Item("class")

		item.value should be ("class")
		item.frequency should be (1)
		item.confidence should be (1)
		item.support should be (0)
	}

	it should "initialize with specified values" in {
		val item = Item("class", 10, 0.5, 0.2)

		item.value should be ("class")
		item.frequency should be (10)
		item.confidence should be (0.5)
		item.support should be (0.2)
	}
	
	it should "compare correctly" in {
		val item1 = Item("class")
		val item2 = Item("class")

		item1 should equal (item2)
	}
}

