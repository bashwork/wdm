package org.school.core.format

import scala.collection.mutable.ListBuffer
import org.school.core.{ItemSet, Transaction, AbstractSupport}

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
     * @return The processed list iterator
     */
    def process(source:Iterator[String]) : List[Transaction[String]] = {
		val transactions = ListBuffer[Transaction[String]]()

		source foreach { _ match {
			case line if line.trim.isEmpty =>
			case line => transactions += buildTransaction(line)
		}}

		transactions.toList
    }

	/**
	 *
	 */
	private def buildTransaction(line:String) : Transaction[String] = {
		val itemsets = ListBuffer[ItemSet[String]]()
		val captures = (matcher findAllIn line).matchData

		captures.foreach { matches =>
			val items = matches.group(1).split(", ")
			itemsets += ItemSet[String](items.toList)
		}
		Transaction[String](itemsets.toList)
	}
}
