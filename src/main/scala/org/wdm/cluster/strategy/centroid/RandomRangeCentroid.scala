package org.wdm.cluster.strategy.centroid

import scala.util.Random.shuffle
import org.wdm.cluster.core.Point

/**
 * A centroid initialization stategty that just picks K
 * random points from the range possible based on the
 * input points:
 *
 * Point(1,2,3) - Point(3,4,5): Point(1-3, 2-4, 3-5)
 */
class RandomRangeCentroid extends CentroidStrategy {

    /**
     * Produce the initial set of centroids to cluster with.
     *
     * @param k The number of centroids to produce
     * @param points The points to derive the centroids from
     * @return The initial centroids to process with
     */
    def apply(k: Int, points:List[Point]) : List[Point] = {
        shuffle(points) take k
    }
}
