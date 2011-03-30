package org.wdm.cluster.core

import org.wdm.distance.Distance
import org.wdm.cluster.strategy.centroid.CentroidStrategy
import org.wdm.cluster.strategy.stopping.StoppingStrategy

/**
 * A collection of the strategy parameters for the kmeans
 * algorithm
 *
 * @param k The number of centroids to create
 * @param points The initial dataset to process
 * @param centroid The centroid initialization strategy
 * @param distance The distance comparison strategy
 * @param stopping The stopping/convergence strategy
 *
 */
class Parameters {
    var k:Int                     = _
    var points:List[Point]        = _
    var distance:Distance         = _
    var centroid:CentroidStrategy = _
    var stopping:StoppingStrategy = _
}
