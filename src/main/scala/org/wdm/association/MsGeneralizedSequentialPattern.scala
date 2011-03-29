package org.wdm.association

import org.slf4j.{Logger, LoggerFactory}
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer
import org.wdm.core.support.AbstractSupport
import org.wdm.core.{ItemSet, Transaction, FrequentSet}

/**
 * This is an implementation of the Minimum Support Generalized Sequential
 * Pattern algorithm outlined in WDM. Given a collection of transactions
 * and an item support lookup table, it will generate a a listing of frequent
 * transactions as well as their support count.
 *
 * @param sequences The collection of transactions to process
 * @param support The support lookup table for each item
 */
class MsGeneralizedSequentialPattern[T](val sequences:List[Transaction[T]],
    val support:AbstractSupport[T]) extends AbstractAssociation[T] {

    /** This is N represented in the gsp algorithm */
    private val sizeN = sequences.size.doubleValue
    private val logger = LoggerFactory.getLogger(this.getClass)
    private var initialL: FrequentSet[T] = _

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

        (2 to Int.MaxValue).toStream.takeWhile(_ => frequents.last.size > 0).foreach { k =>
            logger.debug("generating frequent set: " + k)
            val ck = k match {
                case 2 => candidateGen2(initialL)
                case _ => candidateGenN(frequents.last)
            }
            frequents += buildFrequent(ck)
            logger.debug("generated frequent set {}: size({})", k, frequents.last.size)
        }

        logger.debug("processing took " + stopwatch.toString)
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
        val actual   = HashMap[T,Double]()                                          // I.support
        val unique   = counts.keys.toList                                           // I
        val sorted   = unique.sortWith { (a,b) => support.get(a) < support.get(b) } // M
        sorted.foreach { s => actual(s) = counts(s) / sizeN }                       // populate supports

        val start    = sorted.findIndexOf { s  => actual(s) >= support.get(s) }     // first support > minsup
        val minsup   = support.get(sorted(start))                                   // its index

        val level1   = sorted.slice(start, sorted.size).filter { s =>               // L
            actual(s) >= minsup }
        val filtered = level1.filter { s => actual(s) >= support.get(s) }           // <F1>

        logger.debug("initialization took " + stopwatch.toString)
        logger.debug("generated initial candidates: " + filtered)
        initialL = FrequentSet(level1.map { x => {
            val transaction = Transaction(List(ItemSet(x)), counts(x))
            transaction.support = actual(x)
            transaction.minMisItem = ItemSet(x)
            transaction
        }})

        FrequentSet(filtered.map { x => {                                           // <{F1}>
            val transaction = Transaction(List(ItemSet(x)), counts(x))
            transaction.support = actual(x)
            transaction.minMisItem = ItemSet(x) // not needed
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
                // Todo if c - c{1} in T: c.restCount
            }
        }
        candidates.foreach { candidate =>       // populate support
            candidate.support = candidate.count / sizeN }

        logger.debug("frequent generation took " + stopwatch.toString)
        FrequentSet(candidates.filter { c =>
            c.support >= c.minsup(support) }.distinct)   // <{Fn}>
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
        math.abs { left.support - right.support } <= support.sdc

    /**
     * Helper method to generate the 2-length candidate set
     *
     * @param frequent The previous frequent items to build with(L)
     * @return A possible candidate set to process
     */
    private def candidateGen2(frequent:FrequentSet[T]) : List[Transaction[T]] = {
        implicit def transToType(t:Transaction[T]) : T = t.sets.head.items.head

        val candidates = ListBuffer[Transaction[T]]()
        val transactions = frequent.transactions

        transactions.zipWithIndex.foreach {
            case(l, index) if l.support >= l.minsup(support) => {
            transactions.drop(index + 1).foreach { h =>
                if (h.support >= l.minsup(support) && evaluateSdc(l, h)) {
                    candidates += Transaction(ItemSet[T](l, h)) // <{1,  2}>
                    candidates.last.minMisItem = l.sets.last
                    candidates += Transaction(l.sets ++ h.sets) // <{1},{2}>
                    candidates.last.minMisItem = l.sets.last
                    candidates += Transaction(h.sets ++ l.sets) // <{2},{1}>
                    candidates.last.minMisItem = l.sets.last
                }
            } }
            case(l, index) => logger.debug("candidate2 support({}) not met: {}", l.support, l)
        }

        logger.debug("generated candidates2: " + candidates.toList.distinct)
        candidates.toList.distinct  // remove duplicates
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

        transactions.foreach { left => {
            transactions.foreach { right => {
                candidateCheck(left, right, frequent) match {
                    case Some(candidate) => candidates ++= candidate
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
        frequent:FrequentSet[T]) : Option[List[Transaction[T]]] = {

        val result =
            if (left.minMisItem.items.head == left.sets.head.items.head) {
                 candidateJoinLeft(left, right, frequent)
            } else if (right.minMisItem.items.last == right.sets.last.items.last) {
                 candidateJoinRight(left, right, frequent)
            } else { candidateJoinRegular(left, right, frequent) }

        result match {
            case Some(joins) => candidatePrune(joins, frequent)
            case _           => None
        }
    }

    /**
     * If the first item in the sequence is the minMisItem, we perform
     * this join step.
     *
     * @param left The left transaction to join
     * @param right The right transaction to join
     * @return optionally a joined candidate set
     */
    private def candidateJoinLeft(left:Transaction[T], right:Transaction[T],
        frequent:FrequentSet[T]) : Option[List[Transaction[T]]] = {
        val results = ListBuffer[Transaction[T]]()

        if ((left.without(1) != right.without(-1)) && // check if we will be frequent
             right.minsup(support) > left.minsup(support)) return None

        val lastLeft  = left.sets.last.items
        val lastRight = right.sets.last.items
        if (lastRight.size == 1) { // {1}{2} & {3} => {1}{2}{3}
            results += Transaction(left.sets ++ List(ItemSet(lastRight.last)))
            results.last.minMisItem = left.minMisItem

            if (((left.size == 2) && (left.length == 2)) &&
                (compare(lastRight, lastLeft))) {
                results += Transaction(left.sets.head,                  // {1}{2} & {4} => {1}{2,4}
                    ItemSet(left.sets.last.items.last, lastRight.last))
                results.last.minMisItem = left.minMisItem
            }
        } else if (((left.size == 1) && (left.length == 2)) &&
            (!compare(lastRight.last, lastLeft.last)) ||
            (left.length > 2)) {
            results += Transaction(left.sets ++ List(right.sets.last)) // {1}{2} & {4} => {1}{2}{4}
            results.last.minMisItem = left.minMisItem
        }

        if (results.size > 0) Some(results.toList) else None
    }

    /**
     * If the last item in the second sequence is the minMisItem,
     * we perform * this join step.
     *
     * @param left The left transaction to join
     * @param right The right transaction to join
     * @return optionally a joined candidate set
     */
    private def candidateJoinRight(left:Transaction[T], right:Transaction[T],
        frequent:FrequentSet[T]) : Option[List[Transaction[T]]] = {
        val results = ListBuffer[Transaction[T]]()

        if ((left.without(0) != right.without(-2)) && // check if we will be frequent
             right.minsup(support) < left.minsup(support)) return None

        val firstLeft  = left.sets.head.items
        val firstRight = right.sets.head.items
        if (firstLeft.size == 1) { // {1}{2} & {3} => {1}{2}{3}
            results += Transaction(right.sets ++ List(ItemSet(firstLeft.last)))
            results.last.minMisItem = right.minMisItem

            if (((right.size == 2) && (right.length == 2)) &&
                (compare(firstLeft, firstRight))) {
                results += Transaction(right.sets.head,                 // {1}{2} & {4} => {1}{2,4}
                    ItemSet(right.sets.last.items.last, firstLeft.last))
                results.last.minMisItem = right.minMisItem
            }
        } else if (((right.size == 1) && (right.length == 2)) &&
            (compare(firstLeft.last, firstRight.last)) ||
            (right.length > 2)) {
            results += Transaction(right.sets ++ List(left.sets.last))  // {1}{2} & {4} => {1}{2}{4}
            results.last.minMisItem = right.minMisItem
        }

        if (results.size > 0) Some(results.toList) else None
    }

    /**
     * Helper to test each possible candidate set and return the
     * merged result.
     *
     * @param left The left transaction to join
     * @param right The right transaction to join
     * @return optionally a joined candidate set
     */
    private def candidateJoinRegular(left:Transaction[T], right:Transaction[T],
        frequent:FrequentSet[T]) : Option[List[Transaction[T]]] = {

        left.join(right) match {
            case Some(result) => {
                result.minMisItem = left.minMisItem
                val isGood = result.subsequences.forall { s =>
                    frequent.transactions.exists { t => t contains s }}
                if (isGood) { return Some(List(result)) }
            }
            case None =>  // could not join the two
        }

        return None
    }

    /**
     * Helper to test each possible candidate set and return the
     * merged result.
     *
     * @param left The left transaction to join
     * @param right The right transaction to join
     * @return optionally a joined candidate set
     */
    private def candidatePrune(joins:List[Transaction[T]], frequent:FrequentSet[T])
        : Option[List[Transaction[T]]] = {
        val results = ListBuffer[Transaction[T]]()

        joins.foreach { join => 
            val isGood = join.subsequences.forall { s =>
                val minimum = Transaction(join.minMisItem)
                (frequent.transactions.exists { t => t contains s }) ||
                (!s.contains(minimum)) // missing lowest mis-item is okay
            }
            if (isGood) { results += join }
        }

        if (results.size > 0) Some(results.toList) else None
    }
}
