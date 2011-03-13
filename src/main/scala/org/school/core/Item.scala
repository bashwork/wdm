package org.school.core

import java.io.Serializable

/**
 * Represents a single item from the dataset
 *
 * @param value The value of this item
 * @param frequency The number of occurrences of this item
 * @param confidence The confidence of this item
 * @param support The support of this item
 */
class Item private (val value:String, val frequency:Int, val confidence:Double, val support:Double)
    extends Serializable {

   override def equals(input:Any) =
       input.isInstanceOf[Item] &&
       input.asInstanceOf[Item].value == this.value

}

object Item {
    def apply(value:String) = new Item(value, 1, 1, 0)
    def apply(value:String, frequency:Int, confidence:Double, support:Double)
        = new Item(value, frequency, confidence, support)
}
