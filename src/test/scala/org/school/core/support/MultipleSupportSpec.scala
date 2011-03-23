package org.school.core.support

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class MultipleSupportSpec extends FlatSpec with ShouldMatchers {

	behavior of "A multiple support lookup"

	it should "initialize correctly with a map" in {
		val map = (0 to 3).map { id => (id.toString, id.doubleValue) } toMap
		val mis = MultipleSupport[String](map)

		//mis must haveClass[MultipleSupport]
		mis.get("1") should be (1)
		mis.get("2") should be (2)
		mis.get("3") should be (3)
		mis.sdc should be (0.0)
	}

	it should "initialize correctly with an iterator" in {
		val iterator = Iterator[String]("MIS(1) = 0.01", "SDC = 0.2");
		val mis = MultipleSupport(iterator)

		//mis must haveClass[MultipleSupport]
		mis.get("1") should be (0.01)
		mis.sdc should be  (0.2)
	}
	
}

