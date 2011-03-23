package org.school.core.support

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class SingleSupportSpec extends FlatSpec with ShouldMatchers {

	behavior of "A single support lookup"

	it should "initialize correctly" in {
		val mis = SingleSupport[String](0.2)

		mis should not be (null)
	}

	it should "always return the same support" in {
		val mis = SingleSupport[String](0.2)

		mis.get("1") should be (0.2)
		mis.get("2") should be (0.2)
		mis.get("3") should be (0.2)
	}
}

