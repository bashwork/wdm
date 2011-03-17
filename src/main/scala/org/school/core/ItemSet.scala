package org.school.core

import java.io.Serializable

/**
 * Represents a set of items from the dataset
 *
 * @param items The items composing this Item set
 */
class ItemSet[T] private (val items:Set[T])
    extends Serializable {

    /**
     * Retrieve the minimum support for this transaction set
     *
     * @param support The support lookup table
     * @return The minimum support for this collection
     */
    def minsup(support:AbstractSupport[T]) =
        items.map { support.get(_) }.min

    override def hashCode() = items.hashCode
    override def equals(other:Any) = other match {
        case that: ItemSet[_] => that.items == this.items
        case _ => false
    }

	override def toString() = items.mkString("{", ",", "}")
}


object ItemSet {
    def apply[T](items:List[T]) = new ItemSet[T](items.toSet)
    def apply[T](items:T*) = new ItemSet[T](items.toList.toSet)
}
