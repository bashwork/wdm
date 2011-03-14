package org.school.core.format

import scala.collection.mutable.ListBuffer
import org.school.core.{Item, ItemSet, Transaction, AbstractSupport}

/**
 * Format processor for the sequential format presented by the WDM
 * book and used by the class examples.
 */
object SequentialFormat extends AbstractFormat {

    private val matcher = """<?\{([0-9, ]+)\}>?""".r 

    /**
     * Processes the given source into ItemSets
     *
     * @param source The source to be processed
     * @param support The support lookup table
     * @return The processed list iterator
     */
    def process(source:Iterator[String], support:AbstractSupport) : List[Transaction] = {
		val transactions = ListBuffer[Transaction]()

		source foreach { _ match {
			case line if line.trim.isEmpty =>
			case line => transactions += buildTransaction(line, support)
		}}

		transactions.toList
    }

	/**
	 *
	 */
	private def buildTransaction(line:String, support:AbstractSupport) : Transaction = {
		val itemsets = ListBuffer[ItemSet]()
		val captures = (matcher findAllIn line).matchData

		captures.foreach { matches =>
			val items = matches.group(1).split(", ").map { item =>
				Item(item, 1, 1, support.get(item))
			}
			itemsets += ItemSet(items.toList)
		}
		Transaction(itemsets.toList)
	}
}
