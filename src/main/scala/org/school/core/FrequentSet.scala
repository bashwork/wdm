package org.school.core

import java.io.Serializable

/**
 * Represents a collection of k-length frequent sequence
 * items.
 *
 * @param transactions The k-length frequent patterns
 * @param length The pattern length this represents
 */
class FrequentSet[T] private (val transactions:Map[Transaction[T], Int],
    val length:Int) extends Serializable {

    def size() = transactions.size
}

object FrequentSet {
    def apply[T](items:Map[Transaction[T], Int]) =
        new FrequentSet[T](items, items.head._1.length)
    def apply[T](items:(Transaction[T],Int)*) =
        new FrequentSet[T](items.toMap, items.head._1.length)
}
