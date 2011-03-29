package org.wdm.core.output

import scala.collection.mutable.StringBuilder
import org.wdm.core.RuleSet

/**
 * Output formatter used for outputting association rules
 *
 * @param rules The rules to be processed
 */
class RuleFormatter(rules:List[RuleSet[String]])
    extends AbstractFormatter {

    /**
     * Processes the given source into ItemSets
     *
     * @return The processed string result
     */
    def format() : String = {
		val buffer = new StringBuilder

		rules foreach { rule => 
			buffer.append(rule.toString)
			buffer.append("\n")
		}

		buffer.toString
    }
}
