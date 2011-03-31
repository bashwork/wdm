package org.wdm.distance

/**
 * A method that computes the distance of some
 * points using the euclidian distance formula.
 *
 * Minkowski distance with p == 2
 */
object EuclideanDistance extends Distance {

    def apply(a:Iterable[Double], b:Iterable[Double]) : Double =
        math.sqrt(a.zip(b).foldLeft(0.0) { (total, next) =>
            total + math.pow(next._1 - next._2, 2) })
}
