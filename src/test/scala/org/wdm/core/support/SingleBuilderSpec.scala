package org.wdm.core.support

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.wdm.core.{Transaction, ItemSet}

class SingleBuilderSpec extends FlatSpec with ShouldMatchers {

	behavior of "A single support lookup builder"

	it should "initialize correctly" in {
		val builder = new SingleBuilder[String](0.2)

		builder should not be (null)
	}

	it should "build correctly" in {
        val default  = 0.2
        val items    = List(Transaction(ItemSet("1","2","3")))
		val builder  = new SingleBuilder[String](default)
        val support  = builder.build(items)

        items(0).sets(0).items.foreach { key =>
            support.get(key) should be (default) }
        support.sdc should be (default / 2)
	}
}

