package org.school.association

import org.slf4j.{Logger, LoggerFactory}
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer
import org.school.core.{AbstractSupport}
import org.school.core.{ItemSet, Transaction, FrequentSet}
import org.school.utility.Stopwatch

/**
 * This is an implementation of the Minimum Support Prefix Span algorithm
 * outlined in WDM. Given a collection of transactions * and an item
 * support lookup table, it will generate a a listing of frequent
 * transactions as well as their support count.
 *
 * @param sequences The collection of transactions to process
 * @param support The support lookup table for each item
 */
class PrefixSpan[T](val sequences:List[Transaction[T]],
    val support:AbstractSupport[T]) {

    /** This is N represented in the gsp algorithm */
    private val sizeN = sequences.size.doubleValue
	private val logger = LoggerFactory.getLogger(this.getClass)
	private val stopwatch = new Stopwatch()

    /**
     * Processes the current sequence list to produce all the
     * frequent sequences with the supplied support.
     *
     * @return A list of frequent itemsets
     */
    def process() : List[FrequentSet[T]] = {
        stopwatch.start
        val frequent = initialize()
        val frequents = buildFrequents(frequent)

        logger.info("PrefixSpan processing took " + stopwatch.toString)
        frequents
    }

    /**
     * Generate all the unique items in the list of transactions
     * Note: this comprises the following portions of the algorith:
     * * M: sort(I,MS)
     * * L: init-pass(M, S)
     *
     * @return A list of all the unique items
     */
    private def initialize() : FrequentSet[T] = {
        val allItems = sequences.map { _.unique }.flatten                           // I with repeats
        val counts   = allItems groupBy identity mapValues { _.size }               // I.count
        val unique   = counts.keys.toList                                           // I
        val sorted   = unique.sortWith { (a,b) => support.get(a) < support.get(b) } // L
        val filtered = sorted.filter { i => (counts(i) / sizeN) >= support.get(i) } // <F1>

        logger.debug("PrefixSpan initialization took " + stopwatch.toString)
        logger.debug("generated initial candidates: " + filtered)
        FrequentSet(filtered.map { x =>												// <{F1}>
			Transaction(List(ItemSet(x)), counts(x)) })
    }

    /**
     * Helper method to test that two items exceed the support
     * difference constraint.
     *
     * @param left The left item to test
     * @param right The right item to test
     * @return The result of the test
     */
    private def evaluateSdc(left:Transaction[T], right:Transaction[T]) =
        math.abs { left.minsup(support) - right.minsup(support) } >= support.sdc

    private var default:T = _

    /**
     * Given a possible candidate set, search the sequences to see if any
     * of the candidates are frequent
     *
     * @param candidate The initial candidate to explore
     * @return The frequent candidate list
     */
    private def buildFrequents(candidate:FrequentSet[T]) : List[FrequentSet[T]] = {

		//candidate.transactions.foreach { transaction =>
		//	sequences.foreach { sequence =>
		//		if (sequence contains transaction) {
		//			for (i <- 0 to sequence.size) {
		//			  
		//			    
		//			}
		//		}
		//	}
		//}

        List(FrequentSet(Transaction(ItemSet(default))))
    }

    /**
     * Given a possible candidate set, search the sequences to see if any
     * of the candidates are frequent
     *
     * @param candidate The initial candidate to explore
     * @return The frequent candidate list
     */
    private def buildProjections(source:Transaction[T], frequents:Frequent[T])
		: List[Trancactions[T]] = {
		private def extract(needle:ItemSet[T], source:ItemSet[T]) : ItemSet[T] = {

		}

		private def infrequent(source:Transaction[T]) : Transaction[T] = {
			source
		}
        val possible = ListBuffer[Transaction[T]]()

		sequences.foreach { sequence =>
			if (sequence contains transaction) {
				val last  = transaction.last // since we know the rest is there
				val index = sequence.sets.findIndexOf { set => set contains last }
				val (set :: list) = sequence.sets.drop(index)
				val projection = if (set.size == 1) list
					else (set -- last) :: list
				possible += projection.filter { 
			}
		}

		possible.toList
	}
}
