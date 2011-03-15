package org.school.core.output

import scala.collection.mutable.StringBuilder
import org.school.core.{ItemSet, Transaction}

/**
 * Output formatter used for project 1 sequential rules.
 * example: """
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
object SequentialFormat {

    /**
     * Processes the given source into ItemSets
     *
     * @param source The source to be processed
     * @return The processed list iterator
     */
    def process(results:List[Transaction[String]]) : Iterator[String] = {
		val buffer = new StringBuilder

		results foreach { result =>
			buffer.append("\nThe number of length" + result.size)
			buffer.append(" sequential patterns is " + result.length + "\n")
			result.transactions foreach { transaction =>
				buffer.apppend(buildPattern(transaction))
			}
		}

		buffer.toIterator
    }

	/**
	 * A helper method to format the sequence value correctly
     * @param pattern The pattern to format
     * @return The formatted pattern
     */
	private def buildPattern(pattern:Transaction[String]) : String {
		val buffer = new StringBuilder

		buffer.append("<")
		pattern.sets.foreach { set =>
			val format = set.reduceLeft { (t,s) => t + ", " + s }
			buffer.append("{" + format "}")
		}
		buffer.append("> Count: " + set.count + "\n")

		buffer.toString
	}
}
