package org.school.association

import org.slf4j.{Logger, LoggerFactory}
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer
import org.school.core.{AbstractSupport}
import org.school.core.{ItemSet, Transaction, FrequentSet}

/**
 * This is an implementation of the Minimum Support Generalized Sequential
 * Pattern algorith outlined in WDM. Given a collection of transactions
 * and an item support lookup table, it will generate a a listing of frequent
 * transactions as well as their support count.
 *
 * @param sequences The collection of transactions to process
 * @param support The support lookup table for each item
 */
class GeneralizedSequentialPattern[T](val sequences:List[Transaction[T]],
    val support:AbstractSupport[T]) {

    /** This is N represented in the gsp algorithm */
    private val sizeN = sequences.size.doubleValue
	private val logger = LoggerFactory.getLogger(this.getClass)

    /**
     * Processes the current sequence list to produce all the
     * frequent sequences with the supplied support.
     *
     * @return A list of frequent itemsets
     */
    def process() : List[FrequentSet[T]] = {
        val frequents = ListBuffer[FrequentSet[T]]()
        frequents += initialize()

        (2 to 1000).takeWhile( _ => frequents.last.transactions.size > 0) foreach { k =>
            logger.debug("generating frequent set: " + k)
            val ck = k match {
                case 2 => candidateGen2(frequents.last)
                case _ => candidateGen(frequents.last)
            }
            frequents += buildFrequent(ck)
        }

        frequents.dropRight(1).toList // the last frequent is empty
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

        logger.debug("generated initial candidates: " + filtered)
        FrequentSet(filtered.map { x =>												// <{F1}>
			Transaction(List(ItemSet(x)), counts(x)) })
    }

    /**
     * Given a possible candidate set, search the sequences to see if any
     * of the candidates are frequent
     *
     * @param candidates A collection of possible candidates
     * @return The frequent candidate list
     */
    private def buildFrequent(candidates:List[Transaction[T]]) : FrequentSet[T] = {
        sequences.foreach { sequence =>
            candidates.foreach { candidate =>
                if (sequence contains candidate) {
                    candidate.count += 1
                }
                // if (candidate - minsup(item) in sequence) {
                //    candidate.restCount += 1
                // } this is only used for rule generation TODO
            }
        }

        FrequentSet(candidates.filter { c =>
            (c.count / sizeN) >= c.minsup(support) }) // <{Fn}>
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

    /**
     * Helper method to generate the 2-length candidate set
     *
     * @param frequent The previous frequent items to build with
     * @return A possible candidate set to process
     */
    private def candidateGen2(frequent:FrequentSet[T]) : List[Transaction[T]] = {
        val candidates = ListBuffer[Transaction[T]]()
        val items = frequent.transactions

        items.zipWithIndex.foreach {
			case(l, index) if (l.count / sizeN) >= l.minsup(support) => {
            	items.takeRight(items.size - (index + 1)).foreach { h =>
            	    if ((h.count / sizeN) >= h.minsup(support) && evaluateSdc(l, h)) {
            	        candidates += Transaction(l.sets ++ h.sets)           // <{1},{2}>
            	        candidates += Transaction(l.sets.head, h.sets.head)   // <{1,  2}>
            	    }
            	}
			}
			case(l, index) => logger.debug("candidate2 support({}) not met: {}", (l.count / sizeN), l.sets)
        }
    
        logger.debug("generated candidates2: " + candidates.toList)
        candidates.toList
    }
    
    /**
     * Helper method to generate the N-length candidate set
     *
     * @param frequent The previous frequent items to build with
     * @return A possible candidate set to process
     */
    private def candidateGen(frequent:FrequentSet[T]) : List[Transaction[T]] = {
        val candidates = ListBuffer[Transaction[T]]()
        val items = frequent.transactions

        items.zipWithIndex.foreach {
			case(l, index) if (l.count / sizeN) >= l.minsup(support) => {
            	items.takeRight(items.size - (index + 1)).foreach { h =>
            	    if ((h.count / sizeN) >= h.minsup(support) && evaluateSdc(l, h)) {
            	        candidates += Transaction(l.sets ++ h.sets)           // <{1},{2}>
            	        candidates += Transaction(l.sets.head, h.sets.head)   // <{1,  2}>
            	    }
            	}
			}
			case(l, index) => logger.debug("candidate2 support({}) not met: {}", (l.count / sizeN), l.sets)
        }
    
        logger.debug("generated candidates2: " + candidates.toList)
        candidates.toList
    }
}
