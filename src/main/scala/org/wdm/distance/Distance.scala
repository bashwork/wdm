package org.wdm.distance

/**
 * Represents the base trait for a distance function
 */
trait Distance {
    def apply(a:Double, b:Double) : Double = math.abs(a - b)
    def apply(a:Iterable[Double], b:Iterable[Double]) : Double
    //def apply[T](a:T, b:T)(implicit num:Numeric[T]) : T =
    //    num.abs(num.minus(a, b))
}

