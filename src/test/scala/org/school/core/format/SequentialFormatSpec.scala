package org.school.core.format

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.school.core.SingleSupport
import org.school.core.loader.MemoryLoader

class SequentialFormatSpec extends FlatSpec with ShouldMatchers {

	behavior of "A sequential format parser"

	val example = """
<{25, 37, 47}{48}>
<{44}{44}{45, 47, 49}{23}>
<{18, 23, 37, 44}{18, 46}{17, 42, 44, 49}>
<{26, 43, 48}{5}>
<{11, 38, 43, 48}{9, 43, 44}>
<{30, 48}{49}{26, 34, 45}{36, 39, 42, 47}>
<{36}>
<{29, 45, 48}{34, 44, 48}{29, 34, 40, 42}{8, 17, 32, 46}>
<{46}>"""

	it should "process correctly" in {

		val support = SingleSupport(0.2)
		val loader = MemoryLoader(example)
		val transactions = SequentialFormat.process(loader)

		transactions.size should be (9)
		transactions.foldLeft(0) { (a,b) =>
			a + b.sets.foldLeft(0) { (c,d) => c + d.items.size }
		} should be (57)
	}
}

