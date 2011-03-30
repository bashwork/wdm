package org.wdm.cluster.strategy.centroid

import org.wdm.cluster.core.Point

/**
 * A base trait for a strategy that can be used to produce
 * the initial centroid points for clustering.
 */
trait CentroidStrategy {

    /**
     * Produce the initial set of centroids to cluster with.
     *
     * @param k The number of centroids to produce
     * @param points The points to derive the centroids from
     * @return The initial centroids to process with
     */
    def apply(k:Int, points:List[Point]) : List[Point]
}
