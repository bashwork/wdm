package org.school.core.output

import java.io.File
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.school.core.{ItemSet, Transaction, RuleSet}

class RuleFormatterSpec extends FlatSpec with ShouldMatchers {

	behavior of "A rule output formatter"

    private val expected = """<{1}> -> <{2}>
<{2}{13}> -> <{4}>
<{2,13}> -> <{4}>
"""

	it should "process a frequent set correctly" in {

        val rule1 = RuleSet(Transaction(ItemSet("1")), Transaction(ItemSet("2")))
        val rule2 = RuleSet(Transaction(ItemSet("2"), ItemSet("13")), Transaction(ItemSet("4")))
        val rule3 = RuleSet(Transaction(ItemSet("2", "13")), Transaction(ItemSet("4")))
        val rules = List(rule1, rule2, rule3)
		val formatter = new RuleFormatter(rules)

        formatter.format should be (expected)
	}

	it should "store the result in an output file" in {

        val filename = "rule-output.txt"
        val rule1 = RuleSet(Transaction(ItemSet("1")), Transaction(ItemSet("2")))
        val rules = List(rule1)
		val formatter = new RuleFormatter(rules).toFile(filename)

        val file = new File(filename)
        file.exists should be (true)
        file.delete should be (true)
	}
}

