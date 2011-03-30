package org.wdm.cluster.strategy.stopping

import org.wdm.cluster.core.Point

/**
 * A stopping strategy that causes the clustering algorithm
 * to terminate after count iterations.
 */
class IterationStrategy(var count:Int) {

    /**
     * Produce the initial set of centroids to cluster with.
     *
     * @param centroids The next round of centroids
     * @param points The next round of points
     * @return true if the algorithm should terminate, false otherwise
     */
    def apply(centroids:List[Point], points:List[Point]) : Boolean = {
        count -= 1
        if (count  <= 0) true else false
    }
}
