package org.school.core.support

import org.school.core.Transaction

trait AbstractSupportBuilder[T] {

    /**
     * Given a list of items, builds a multiple support
     * lookup table based on a number of strategies.
     *
     * @param items The items to build a support lookup for
     * @param sizeN The total number of transactions in the dataset
     * @return The populated support lookup table
     */
    protected def process(items:Map[T, Int], sizeN:Int) : MultipleSupport[T]

    /**
     * Given a list of items, builds a multiple support
     * lookup table based on a number of strategies.
     *
     * @param items The items to build a support lookup for
     * @return The populated support lookup table
     */
    def build(transactions:List[Transaction[T]]) : MultipleSupport[T] = {
        val items  = transactions.map { _.unique }.flatten        // I with repeats
        val counts = items groupBy identity mapValues { _.size }  // I.count

        process(counts, transactions.size)
    }
}
