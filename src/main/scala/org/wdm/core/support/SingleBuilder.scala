package org.wdm.core.support

/**
 * A multiple support builder that uses a single
 * value for all the intput values.
 */
class SingleBuilder[T](val default:Double = 1.0)
    extends AbstractSupportBuilder[T] {

    /**
     * Given a list of items, builds a multiple support
     * lookup table based on a number of strategies.
     *
     * @param items The items to build a support lookup for
     * @param sizeN The total number of transactions in the dataset
     * @return The populated support lookup table
     */
    protected override def process(items:Map[T, Int], sizeN:Int) : MultipleSupport[T] = {
        val (min,max) = (Double.MaxValue, 0.0)
        val supports = items.map { case (item, _) =>
            (item, default) }

        val support = MultipleSupport[T](supports)
        support.sdc = default / 2
        support
    }
}
