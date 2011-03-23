package org.school.core.support

import org.school.core.Transaction

class FrequencyBuilder[T](val sdcScale:Double = 10.0)
    extends AbstractSupportBuilder[T] {

    /**
     * Given a list of items, builds a multiple support
     * lookup table based on a number of strategies.
     *
     * @param items The items to build a support lookup for
     * @param sizeN The total number of transactions in the dataset
     * @return The populated support lookup table
     */
    protected def process(items:Map[T, Int], sizeN:Int) : MultipleSupport[T] = {
        val supports = items.map { case (item, count) => {
            (item, count/(sizeN * 1.0))
        }}
        val max = supports.values.reduceLeft { math.max }
        val min = supports.values.reduceLeft { math.min }

        val support = MultipleSupport[T](supports)
        support.sdc = (max - min) * sdcScale
        support
    }
}
