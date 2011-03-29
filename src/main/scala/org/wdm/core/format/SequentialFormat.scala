package org.wdm.core.format

import scala.collection.mutable.ListBuffer
import org.wdm.core.{ItemSet, Transaction}

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
     * A helper method that extracts the next sequential rule
       *
     * @param line The line to parse for the next rule
     * @return A transaction for the specified line
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
