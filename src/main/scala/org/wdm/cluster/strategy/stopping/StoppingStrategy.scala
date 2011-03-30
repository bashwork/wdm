package org.wdm.cluster.strategy.stopping

import org.wdm.cluster.core.Point

/**
 * A base trait for a strategy that can be used to check if
 * a clusting algorithm has terminated.
 */
trait StoppingStrategy {

    /**
     * Produce the initial set of centroids to cluster with.
     *
     * @param centroids The next round of centroids
     * @param points The next round of points
     * @return true if the algorithm should terminate, false otherwise
     */
    def apply(centroids:List[Point], points:List[Point]) : Boolean
}
