package org.school.core

import java.io.Serializable

/**
 * Represents a set of items from the dataset
 *
 * @param items The items composing this Item set
 */
class ItemSet[T] private (val items:List[T])
    extends Serializable {

    /** The size of the ItemSet */
    def size = items.size

    /** The support of the ItemSet */
    var support = 0.0

    /** If a template variable exists, its index */
    var templateIndex = 0xbadbeef // poisoned to never be less than

    /**
     * Helper method to check if an item is a template index
     * (used for prefix span). Note, although the value may
     * not be 0, it may still be a template value.
     *
     * @param item The item to check
     * @return true if template, false otherwise
     */
    def isTemplate(item:T) =
        items.findIndexOf { _ == item } == templateIndex

    /**
     * Retrieve the minimum support for this transaction set
     *
     * @param support The support lookup table
     * @return The minimum support for this collection
     */
    def minsup(support:AbstractSupport[T]) =
        items.map { support.get(_) }.min

    /**
     * Checks if the supplied itemset is contained
     * in this one.
     *
     * @param other The other itemset to test
     * @return true if successful, false otherwise
     */
    def contains(other:ItemSet[T]) =
        other.items.forall { o => items.contains(o) }

    override def hashCode() = items.hashCode
    override def equals(other:Any) = other match {
        case that: ItemSet[_] => that.items == this.items
        case _ => false
    }

    override def toString() = items.mkString("{", ",", "}")
}

object ItemSet {
    def apply[T](items:List[T]) = new ItemSet[T](items.distinct)
    def apply[T](items:T*) = new ItemSet[T](items.toList.distinct)
}
