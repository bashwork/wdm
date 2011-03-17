package org.school.core

import java.io.Serializable

/**
 * Represents a collection of k-length frequent sequence
 * items.
 *
 * @param transactions The k-length frequent patterns
 * @param length The pattern length this represents
 */
class FrequentSet[T] private (val transactions:List[Transaction[T]],
    val length:Int) extends Serializable {

    def size() = transactions.size
    def unique() = transactions.map { _.unique }.flatten

	override def toString() = transactions.mkString("\n")
}

object FrequentSet {
    def apply[T](items:List[Transaction[T]]) =
        new FrequentSet[T](items, items.head.length)
    def apply[T](items:Transaction[T]*) =
        new FrequentSet[T](items.toList, items.head.length)
}
