package org.wdm.distance

/**
 * A method that computes the distance of some
 * points using the Chebyshev distance formula.
 *
 * Minkowski distance with p reaching infinity.
 */
object ChebyshevDistance extends Distance {

    override def apply(a:Iterable[Double], b:Iterable[Double]) : Double =
        a.zip(b).foldLeft(0.0) { (prev, next) =>
            math.max(prev, apply(next._1, next._2)) }

    override def apply[X:ClassManifest](a:Iterable[Int], b:Iterable[Int]) : Double =
        a.zip(b).foldLeft(0.0) { (prev, next) =>
            math.max(prev, apply(next._1, next._2)) }
}
