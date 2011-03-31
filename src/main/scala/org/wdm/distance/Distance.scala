package org.wdm.distance

/**
 * Represents the base trait for a distance function.
 *
 * The ClassManifest is used to get around scala's type
 * erasure.
 */
trait Distance {
    def apply(a:Double, b:Double) : Double = math.abs(a - b)
    def apply(a:Int, b:Int) : Double = math.abs(a - b)
    def apply(a:Iterable[Double], b:Iterable[Double]) : Double
    def apply[X:ClassManifest](a:Iterable[Int], b:Iterable[Int]) : Double =
        apply(a.map { _.toDouble }, b.map { _.toDouble })
}

