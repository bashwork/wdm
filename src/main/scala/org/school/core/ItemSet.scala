package org.school.core

import java.io.Serializable

/**
 * Represents a set of items from the dataset
 *
 * @param items The items composing this Item set
 */
class ItemSet private (val items:Set[Item])
    extends Serializable {

    def minsup()  = items.map { _.support }.min
    def minconf() = items.map { _.confidence }.min
}

object ItemSet {
    def apply(items:List[Item]) = new ItemSet(items.toSet)
    def apply(items:Item*) = new ItemSet(items.toList.toSet)
}
