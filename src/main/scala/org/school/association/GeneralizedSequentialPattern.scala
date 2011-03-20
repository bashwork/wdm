package org.school.association

import org.slf4j.{Logger, LoggerFactory}
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer
import org.school.core.{AbstractSupport}
import org.school.core.{ItemSet, Transaction, FrequentSet}
import org.school.utility.Stopwatch

/**
 * This is an implementation of the Minimum Support Generalized Sequential
 * Pattern algorithm outlined in WDM. Given a collection of transactions
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
	private val stopwatch = new Stopwatch()

    /**
     * Processes the current sequence list to produce all the
     * frequent sequences with the supplied support.
     *
     * @return A list of frequent itemsets
     */
    def process() : List[FrequentSet[T]] = {
        val frequents = ListBuffer[FrequentSet[T]]()
        stopwatch.start
        frequents += initialize()

        (2 to 1000).toStream.takeWhile(_ => frequents.last.size > 0).foreach { k =>
            logger.debug("generating frequent set: " + k)
            val ck = k match {
                case 2 => candidateGen2(frequents.last)
                case _ => candidateGenN(frequents.last)
            }
            frequents += buildFrequent(ck)
            logger.info("generated frequent set {}: size({})", k, frequents.last.size)
        }

        logger.debug("GSP processing took " + stopwatch.toString)
        frequents.init.toList // the last frequent is empty
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
        //val actual   = allItems groupBy identity mapValues { 0.0 }               // I.support
        val unique   = counts.keys.toList                                           // I
        val sorted   = unique.sortWith { (a,b) => support.get(a) < support.get(b) } // L
        val filtered = sorted.filter { i => (counts(i) / sizeN) >= support.get(i) } // <F1>

        logger.debug("GSP initialization took " + stopwatch.toString)
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
    def buildFrequent(candidates:List[Transaction[T]]) : FrequentSet[T] = {
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

        logger.debug("GSP frequent generation took " + stopwatch.toString)
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
        val transactions = frequent.transactions
		implicit def transToType(t:Transaction[T]) : T = t.sets.head.items.head	

        transactions.zipWithIndex.foreach {
			case(l, index) if (l.count / sizeN) >= l.minsup(support) => {
            	transactions.takeRight(transactions.size - (index + 1)).foreach { h =>
            	    if ((h.count / sizeN) >= h.minsup(support) && evaluateSdc(l, h)) {
            	        candidates += Transaction(ItemSet[T](l, h)) // <{1,  2}>
            	        candidates += Transaction(l.sets ++ h.sets) // <{1},{2}>
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
    def candidateGenN(frequent:FrequentSet[T]) : List[Transaction[T]] = {
        val candidates = ListBuffer[Transaction[T]]()
        val transactions = frequent.transactions

        transactions.zipWithIndex.foreach { case(left, lindex) => {
        	transactions.zipWithIndex.foreach {
				case(right, rindex) if lindex != rindex => {
					candidateCheck(left, right, frequent) match {
						case Some(candidate) => candidates += candidate
						case None => // these two did not join
					}
				}
				case _ => // we can't join the same transaction
			}
        } }
    
        logger.debug("generated candidatesN: " + candidates.toList)
        candidates.toList
    }

    /**
     * Helper to test each possible candidate set and return the
     * merged result.
     *
     * @param left The left transaction to join
     * @param right The right transaction to join
     * @return optionally a joined candidate set
     */
	private def candidateCheck(left:Transaction[T], right:Transaction[T],
		frequent:FrequentSet[T]) : Option[Transaction[T]] = {

		left.join(right) match {
			case Some(result) => {
				if (result.subsequences.forall { s =>
					frequent.transactions.exists { t => t contains s }}) {
					return Some(result)
				}
			}
			case None =>  // could not join the two
		}

		None
	}
}
