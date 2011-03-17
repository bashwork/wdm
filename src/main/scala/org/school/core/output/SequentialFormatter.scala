package org.school.core.output

import scala.collection.mutable.StringBuilder
import org.school.core.{ItemSet, Transaction, FrequentSet}

/**
 * Output formatter used for project 1 sequential rules.
 *
 * @param frequents The source to be processed
 * @example """
 *
 * The number of length 1 sequential patterns is 2
 * <{1}> Count: 1
 * <{2}> Count: 4
 * 
 * The number of length 2 sequential patterns is 1
 * <{2}{13}> Count: 1
 * 
 * The number of length 3 sequential patterns is 2
 * <{2}{13}{14}> Count: 1
 * <{2,13}{13}> Count: 1
 * """
 */
class SequentialFormatter(frequents:List[FrequentSet[String]])
    extends AbstractFormatter {

    /**
     * Processes the given source into ItemSets
     *
     * @return The processed string result
     */
    def format() : String = {
		val buffer = new StringBuilder

		frequents foreach { frequent =>
			buffer.append("\nThe number of length " + frequent.length)
			buffer.append(" sequential patterns is " + frequent.size + "\n")
			frequent.transactions foreach { transaction =>
				buildPattern(transaction, buffer)
			}
		}

		buffer.toString
    }

	/**
	 * A helper method to format the sequence value correctly
     * @param pattern The pattern to format
     * @return The formatted pattern
     */
	private def buildPattern(pattern:Transaction[String],
        buffer:StringBuilder) {

		buffer.append("<")
		pattern.sets.foreach { set =>
			val format = set.items.reduceLeft { (t,s) => t + "," + s }
			buffer.append("{" + format + "}")
		}
		buffer.append("> Count: " + pattern.count + "\n")
	}
}
