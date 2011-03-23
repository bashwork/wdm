package org.school.core.support

trait AbstractSupport[T] {

    /**
     * Reprsents support difference constaint
     */
    var sdc: Double = 0.0

    /**
     * Retrieves the requested value for the specified key
     *
     * @param key The key to find the value for
     * @return the value for the specified input
     */
    def get(key:T) : Double
}
