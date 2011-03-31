package org.wdm.distance

/**
 * A method that computes the distance of some
 * points using the euclidian squared distance
 * formula (which gives more weight to outliers)
 */
object EuclideanSquaredDistance extends Distance {

    def apply(a:Iterable[Double], b:Iterable[Double]) : Double =
        a.zip(b).foldLeft(0.0) { (total, next) =>
            total + math.pow(next._1 - next._2, 2) }
}
