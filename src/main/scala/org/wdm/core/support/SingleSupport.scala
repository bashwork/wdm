package org.wdm.core.support

/**
 * Implements a support lookup that has a single support value
 */
class SingleSupport[T] private (val support:Double) extends AbstractSupport[T] {

    /**
     * Retrieves the requested value for the specified key
     *
     * @param key The key to find the value for
     * @return the value for the specified input
     */
    override def get(key:T) = support
}

object SingleSupport {
    def apply[T](support:Double) = new SingleSupport[T](support)
}
