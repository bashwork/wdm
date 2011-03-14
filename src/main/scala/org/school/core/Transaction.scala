package org.school.core

import java.io.Serializable

/**
 * Represents a sequence of itemsets
 *
 * @param sets The itemsets composing this Transaction
 */
class Transaction[T] private (val sets:List[ItemSet[T]])
    extends Serializable {

    def unique()   = sets.map { _.items }.flatten.toSet
    def allItems() = sets.map { _.items }.flatten.toList

    /**
     * Retrieve the minimum support for this transaction set
     *
     * @param support The support lookup table
     * @return The minimum support for this collection
     */
    def minsup(support:AbstractSupport[T]) =
        sets.map { s => s.minsup(support) }.min
}

object Transaction {
    def apply[T](items:List[ItemSet[T]]) = new Transaction[T](items)
    def apply[T](items:ItemSet[T]*) = new Transaction[T](items.toList)
}
