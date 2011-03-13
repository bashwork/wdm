package org.school.core.format

import org.school.core.{Item, ItemSet, Transaction, AbstractSupport}

/**
 */
object SequentialFormat extends AbstractFormat {

    private val matcher = """<?\{([0-9, ]+)\}>?""".r 

    /**
     * Processes the given source into ItemSets
     *
     * @param source The source to be processed
     * @param lookup The support lookup table
     * @return The processed list iterator
     */
    def process(source:Iterator[String], lookup:AbstractSupport) : List[Transaction] = {
		val transactions = source.map { line =>
			val itemsets = (matcher findAllIn line).matchData.map { raw =>
				var items = raw.group(1).split(", ").map {
					item => Item(item, 1, 1, lookup.get(item))
				}
				ItemSet(items.toList)
			}
			Transaction(itemsets.toList)
		}
    	transactions.toList
    }
}
