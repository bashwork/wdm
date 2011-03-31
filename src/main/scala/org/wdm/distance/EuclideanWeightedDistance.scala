package org.wdm.distance

/**
 * A method that computes the distance of some
 * points using the euclidian weighted distance
 * formula (which gives more weight to certain points
 * over others).
 */
object EuclideanWeightedDistance {

    def apply(a:Iterable[Double], b:Iterable[Double],
        w:Iterable[Double]) : Double =
        a.zip(b).zip(w).foldLeft(0.0) { (total, next) =>
            total + (math.pow(next._1._1 - next._1._2, 2) * next._2) }
}
