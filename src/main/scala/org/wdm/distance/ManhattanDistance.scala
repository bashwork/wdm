package org.wdm.distance

/**
 * A method that computes the distance of some
 * points using the Manhattan distance formula.
 *
 * Minkowski distance with p == 1
 */
object ManhattanDistance extends Distance {

    override def apply(a:Iterable[Double], b:Iterable[Double]) : Double =
        a.zip(b).foldLeft(0.0) { (total, next) =>
            total + apply(next._1, next._2) }
}
