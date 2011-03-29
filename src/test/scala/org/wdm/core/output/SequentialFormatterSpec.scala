package org.wdm.core.output

import java.io.File
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.wdm.core.{ItemSet, Transaction, FrequentSet}

class SequentialFormatterSpec extends FlatSpec with ShouldMatchers {

	behavior of "A sequential output formatter"

    private val expected = """
The number of length 1 sequential patterns is 2
<{1}> Count: 1
<{2}> Count: 2

The number of length 2 sequential patterns is 1
<{2}{13}> Count: 1

The number of length 3 sequential patterns is 2
<{2,13}{13}> Count: 1
<{2}{13}{14}> Count: 2
"""

	it should "process a frequent set correctly" in {

        val t1 = List(Transaction(ItemSet("1")), Transaction(ItemSet("2")))
        val t2 = List(Transaction(ItemSet("2"), ItemSet("13")))
        val t3 = List(Transaction(ItemSet("2", "13"), ItemSet("13")),
                     Transaction(ItemSet("2"), ItemSet("13"), ItemSet("14")))

		t1(0).count = 1;
		t1(1).count = 2;
		t2(0).count = 1;
		t3(0).count = 1;
		t3(1).count = 2;

        val frequents = List(FrequentSet[String](t1), FrequentSet[String](t2), FrequentSet[String](t3))
		val formatter = new SequentialFormatter(frequents)

        formatter.format should be (expected)
	}

	it should "store the result in an output file" in {

        val filename = "sequential-output.txt"
        val t1 = List(Transaction(ItemSet("1")), Transaction(ItemSet("2")))
        val frequents = List(FrequentSet[String](t1))
		val formatter = new SequentialFormatter(frequents).toFile(filename)

        val file = new File(filename)
        file.exists should be (true)
        file.delete should be (true)
	}
}

