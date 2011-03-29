package org.wdm.distance

/**
 * A method that computes the distance of some
 * points using the Chebyshev distance formula.
 *
 * Minkowski distance with p reaching infinity.
 */
object ChebyshevDistance extends Distance {

    def apply(a:Iterable[Double], b:Iterable[Double]) =
        a.zip(b).foldLeft(Double.MinValue) { (prev, next) =>
            math.max(prev, apply(next._1, next._2)) }
}
