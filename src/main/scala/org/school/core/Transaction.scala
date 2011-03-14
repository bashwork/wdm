package org.school.core

import java.io.Serializable

/**
 * Represents a sequence of itemsets
 *
 * @param sets The itemsets composing this Transaction
 */
class Transaction private (val sets:List[ItemSet])
    extends Serializable {

    def candidates() : Map[Item, Int] = {

        val everything = sets.map { _.items }.flatten.toList
        everything groupBy identity mapValues { _.size }
    }
}

object Transaction {
    def apply(items:List[ItemSet]) = new Transaction(items)
    def apply(items:ItemSet*) = new Transaction(items.toList)
}
