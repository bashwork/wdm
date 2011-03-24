package org.school.association

import org.school.core.FrequentSet
import org.school.utility.Stopwatch

/**
 * Represents an association pattern interface along with a number
 * of helper methods.
 */
trait AbstractAssociation[T] {

    /** A stopwatch used to time the algorithm processing */
    protected val stopwatch = new Stopwatch()

    /**
     * Processes the current sequence list to produce all the
     * frequent sequences with the supplied support.
     *
     * @return A list of frequent itemsets
     */
    def process() : List[FrequentSet[T]]

    /**
     * So scala generics are pretty pathetic. Basically they use type
     * erasure to maintain backwards compatability with the legacy JVM.
     * This means that we actually cannot compare the inner types directly
     * as we don't know what they are! Since we know that we are comparing
     * something of digits (at least for this class) we can just:
     * Any.toString.toInt and compare that
     *
     * @param left The left item to check
     * @param right the right item to check
     * @return true if left is greater than right
     */
    protected def compare(left:T, right:T) =
        (left.toString.toInt < right.toString.toInt)

    /**
     * So scala generics are pretty pathetic. Basically they use type
     * erasure to maintain backwards compatability with the legacy JVM.
     * This means that we actually cannot compare the inner types directly
     * as we don't know what they are! Since we know that we are comparing
     * something of digits (at least for this class) we can just:
     * Any.toString.toInt and compare that
     *
     * @param left The left list to check
     * @param right the right list to check
     * @return true if left is greater than right
     */
    protected def compare(left:List[T], right:List[T]) =
        (left.last.toString.toInt > right.last.toString.toInt)
}
