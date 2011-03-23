package org.school.association

import org.slf4j.{Logger, LoggerFactory}
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer
import org.school.core.{AbstractSupport}
import org.school.core.{ItemSet, Transaction, FrequentSet}
import org.school.utility.Stopwatch

/**
 * This is an implementation of the Apriori algorithm outlined in WDM.
 * Given a collection of transactions * and an item support lookup
 * table, it will generate a a listing of frequent * transactions as
 * well as their support count.
 *
 * @param sequences The collection of transactions to process
 * @param support The support lookup table for each item
 */
class Apriori[T](val sequences:List[Transaction[T]],
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

        while(frequents.last.size > 0) {
            val ck = candidateGen(frequents.last)
            frequents += buildFrequent(ck)
            logger.debug("generated frequent set: size({})", frequents.last.size)
        }

        logger.debug("processing took " + stopwatch.toString)
        frequents.init.toList // the last frequent is empty
    }

    /**
     * So scala generics are pretty pathetic. Basically they use type
     * erasure to maintain backwards compatability with the legacy JVM.
     * This means that we actually cannot compare the inner types directly
     * as we don't know what they are! Since we know that we are comparing
     * something of digits (at least for this class) we can just:
     * Any.toString.toInt and compare that
     *
     * @param l The left item to check
     * @param r the right item to check
     * @return true if l is greater than r
     */
    private def erasureCheck(l:T, r:T) =
        l.toString.toInt < r.toString.toInt

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
        val actual   = HashMap[T,Double]()                                          // I.support
        val unique   = counts.keys.toList                                           // I
        val sorted   = unique.sortWith { (a,b) => erasureCheck(a,b) }               // M
        sorted.foreach { s => actual(s) = counts(s) / sizeN }                       // populate supports
        val filtered = sorted.filter { s => actual(s) >= support.get(s) }           // <F1>

        logger.debug("initialization took " + stopwatch.toString)
        logger.debug("generated initial candidates: " + filtered)

        FrequentSet(filtered.map { x => {                                           // <{F1}>
            val transaction = Transaction(List(ItemSet(x)), counts(x))
            transaction.support = actual(x)
            transaction
       }})
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
            }
        }
        candidates.foreach { candidate =>       // populate support
            candidate.support = candidate.count / sizeN }

        logger.debug("frequent generation took " + stopwatch.toString)
        FrequentSet(candidates.filter { c =>
            c.support >= c.minsup(support) }.distinct)   // <{Fn}>
    }

    /**
     * Helper method to generate the N-length candidate set
     *
     * @param frequent The previous frequent items to build with
     * @return A possible candidate set to process
     */
    def candidateGen(frequent:FrequentSet[T]) : List[Transaction[T]] = {
        val candidates = ListBuffer[Transaction[T]]()
        val transactions = frequent.transactions

        transactions.foreach { left => {
            transactions.foreach { right => {
                candidateCheck(left, right, frequent) match {
                    case Some(candidate) => candidates += candidate
                    case None => // these two did not join
                }
            } }
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

        candidateJoin(left, right, frequent) match {
            case Some(join) => candidatePrune(join, frequent)
            case _          => None
        }
    }

    /**
     * Helper to test each possible candidate set and return the
     * merged result.
     *
     * @param left The left transaction to join
     * @param right The right transaction to join
     * @return optionally a joined candidate set
     */
    private def candidateJoin(left:Transaction[T], right:Transaction[T],
        frequent:FrequentSet[T]) : Option[Transaction[T]] = {

       //if (left < right) Some(Transaction(left.sets +
       //    ItemSet(right.sets.last.items.last))) else None
        None
    }

    /**
     * Helper to test the possible candidate set and return
     * the merged result.
     *
     * @param join The joined transaction to test
     * @return optionally a joined candidate set
     */
    private def candidatePrune(join:Transaction[T], frequent:FrequentSet[T])
        : Option[Transaction[T]] = {

        val isGood = join.subsequences.forall { s =>
            (frequent.transactions.exists { t => t contains s })
        }

        if (isGood) Some(join) else None
    }
}
