package org.wdm.distance

/**
 * A method that computes the distance of some
 * points using the euclidian weighted distance
 * formula (which gives more weight to certain points
 * over others).
 */
object EuclideanWeightedDistance {

    def apply(a:Iterable[Double], b:Iterable[Double]) =
        a.zip(b).foldLeft(0.0) { (total, next) =>
            total + math.pow(next._1 - next._2, 2) }

    def apply[X:ClassManifest](a:Iterable[Int], b:Iterable[Int]) =
        a.zip(b).foldLeft(0.0) { (total, next) =>
            total + math.pow(next._1 - next._2, 2) }
}
