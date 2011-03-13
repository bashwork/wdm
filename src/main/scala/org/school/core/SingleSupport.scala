package org.school.core

/**
 * Implements a support lookup that has a single support value
 */
class SingleSupport private (val support:Double) extends AbstractSupport {

    /**
     * Retrieves the requested value for the specified key
     *
     * @param key The key to find the value for
     * @return the value for the specified input
     */
    override def get(key:String) = support
}

object SingleSupport {
    def apply(support:Double) = new SingleSupport(support)
}
