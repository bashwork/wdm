package org.wdm.core

import java.io.Serializable

/**
 * Represents a single labeld item that can be used
 * in an itemset if needed.
 *
 * @param value The value of the item
 * @param label The label associated with this item
 */
class Item[T] private (val value:T, val label:String)
    extends Serializable {

    override def hashCode() = value.hashCode + label.hashCode
    override def equals(other:Any) = other match {
        case that: Item[_] => (that.value == this.value) &&
                              (that.label == this.label)
        case _ => false
    }

    override def toString() = "(" + label + "," + value + ")"
}

/**
 * Companion object for the item set class
 */
object Item {
    def apply[T](value:T, label:String) = new Item[T](value, label)
}
