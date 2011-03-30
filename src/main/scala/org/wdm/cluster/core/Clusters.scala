package org.wdm.cluster.core

import java.io.Serializable

/**
 * Represents a single N-axis data point
 *
 * @param points The axis points of the given data point
 */
class Clusters private (val points:List[Point],
    val centroids:List[Point]) extends Serializable {

    /** The size of the ItemSet */
    def size = centroids.size

    override def toString() = centroids.mkString("{", " ", "}")
    override def hashCode() = centroids.hashCode
    override def equals(other:Any) = other match {
        case that: Clusters => that.centroids == this.centroids
        case _ => false
    }
}

/**
 * Companion object for the data point class
 */
object Clusters {
    def apply(points:List[Point], centroids:List[Point])
        = new Clusters(points, centroids)
}
