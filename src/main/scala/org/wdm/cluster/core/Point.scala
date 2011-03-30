package org.wdm.cluster.core

import java.io.Serializable

/**
 * Represents a single N-axis data point
 *
 * @param points The axis points of the given data point
 */
class Point private (val points:List[Int])
    extends Serializable {

    /** The current cluster label of this point */
    var label:String = _
    var distance:Double = Double.MaxValue

    /** The size of the ItemSet */
    def size = points.size

    override def toString() = points.mkString("(", ",", ")")
    override def hashCode() = points.hashCode
    override def equals(other:Any) = other match {
        case that: Point => that.points == this.points
        case _ => false
    }
}

/**
 * Companion object for the data point class
 */
object Point {
    def apply(points:List[Int]) = new Point(points)
    def apply(points:Int*) = new Point(points.toList)
}
