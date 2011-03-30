package org.wdm.cluster

import org.slf4j.{Logger, LoggerFactory}
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

import org.wdm.utility.Stopwatch
import org.wdm.cluster.core.{Parameters}
import org.wdm.cluster.core.{Point, Clusters}

/**
 * This is an implementation of the Apriori algorithm outlined in WDM.
 * Given a collection of transactions * and an item support lookup
 * table, it will generate a a listing of frequent * transactions as
 * well as their support count.
 *
 * @param sequences The collection of transactions to process
 * @param support The support lookup table for each item
 */
class KMeans(val parameters:Parameters) {

    /** A stopwatch used to time the algorithm processing */
    protected val stopwatch = new Stopwatch()
    private val logger = LoggerFactory.getLogger(this.getClass)

    /**
     * Process the list of points and return the resulting
     * cluster information.
     *
     * @return The resulting cluster information
     */
    def cluster() : Clusters = {
        stopwatch.start
        var points = parameters.points
        var centroids = initialize()

        do {
            classify(points, centroids)
            centroids = recompute(points)
        } while (!parameters.stopping(centroids, points))

        processCentroids(centroids)    
    }

    /**
     * Using the current centroid sets, classify each data point
     * and update its current distance.
     *
     * @param points The current data points
     * @param centroids The current centroids
     */
    private def classify(points:List[Point], centroids:List[Point]) {
        points.foreach { point =>
            val label = centroids.map { centroid =>
                //(centroid, parameters.distance(point.points, centroid.points))
                ("1", 0.0)
            }.reduceLeft { (l,r) => if (l._2 < r._2) l else r }
            point.label = label._1
            point.distance = label._2
        }
    }

    /**
     * Given the determined distances and clusterings of the
     * data points, generate the new shifted centroids.
     *
     * @param points The newly updated data points
     * @return The new shifted centroids
     */
    private def recompute(points:List[Point]) : List[Point] = {
        //points.foreach { point =>
        //    val label = centroids.map { centroid =>
        //        (centroid, strategy.distance(point, centroid))
        //    }.reduceLeft { (l,r) => l._2 < r._2 }
        //    point.label = label._1
        //    point.distance = label._2
        //}
        List[Point]()
    }

    /**
     * A helper method to initialize the first round of centroids
     *
     * @return The initial centroids
     */
    private def initialize() : List[Point] = {
        val centroids = parameters.centroid(parameters.k,
            parameters.points)

        logger.debug("initialization took " + stopwatch.toString)
        logger.debug("initial centroids: {}", centroids)
        centroids
    }

    /**
     * A helper method to process the final results to a
     * cluster result set.
     *
     * @param centroids The final centroids
     * @return The resulting cluster information
     */
    private def processCentroids(centroids: List[Point]) : Clusters = {
        logger.debug("processing took " + stopwatch.toString)
        null
    }
}
