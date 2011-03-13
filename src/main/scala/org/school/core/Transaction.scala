package org.school.core

import java.io.Serializable

/**
 * Represents a sequence of itemsets
 *
 * @param items The items composing this Item set
 */
class Transaction private (val sets:List[ItemSet])
    extends Serializable {

    //def minsup()  = items.map { _.support }.min
    //def minconf() = items.map { _.confidence }.min
}

object Transaction {
    def apply(items:List[ItemSet]) = new Transaction(items)
    def apply(items:ItemSet*) = new Transaction(items.toList)
}
