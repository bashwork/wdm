package org.school.association

import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer
import org.school.core.{AbstractSupport}
import org.school.core.{ItemSet, Transaction, FrequentSet}

/**
 *
 * @param sequences The collection of transactions to process
 * @param support The support lookup table for each item
 */
class GeneralizedSequentialPattern[T](val sequences:List[Transaction[T]],
    val support:AbstractSupport[T]) {

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
        val total    = sequences.size.doubleValue                                   // S.size == n
        val allItems = sequences.map { _.unique }.flatten                           // I with repeats
        val counts   = allItems groupBy identity mapValues { _.size }               // I.count
        val unique   = counts.keys.toList                                           // I
        val sorted   = unique.sortWith { (a,b) => support.get(a) < support.get(b) } // L
        val filtered = sorted.filter { i => (counts(i) / total) >= support.get(i) } // <F1>

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
        val total  = sequences.size                         // S.size == n
        val counts = HashMap[Transaction[T], Int]()

        sequences.map { _.sets }.flatten.foreach { sequence =>
            candidates.foreach { candidate =>
                if (sequence == candidate) {
                    counts.get(candidate) match {
                        case None => counts.put(candidate, 0)
                        case Some(_) => counts(candidate) += 1
                    }
                }
                // two other cases
            }
        }

        FrequentSet(candidates.filter { c =>
            (counts(c) / total) >= c.minsup(support) }) // <{Fn}>
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
        val n = sequences.size
        val candidates = ListBuffer[Transaction[T]]()
        val items = frequent.transactions

        items.zipWithIndex.foreach { case(l, index) if (l.count / n) >= l.minsup(support) =>
            items.takeRight(items.size - index + 1).foreach { h =>
                if ((h.count / n) >= h.minsup(support) && evaluateSdc(l, h)) {
                    candidates += Transaction(l.sets ++ h.sets) // {1} ++ {2} -> {1, 2}
                }
            }
        }
    
        candidates.toList
    }
    
    /**
     * Helper method to generate the N-length candidate set
     *
     * @param frequent The previous frequent items to build with
     * @return A possible candidate set to process
     */
    private def candidateGen(frequent:FrequentSet[T]) : List[Transaction[T]] = {
        val n = sequences.size
        val candidates = ListBuffer[Transaction[T]]()
        val items = frequent.transactions

        items.zipWithIndex.foreach { case(l, index) if (l.count / n) >= l.minsup(support) =>
            items.takeRight(items.size - index + 1).foreach { h =>
                if ((h.count / n) >= h.minsup(support) && evaluateSdc(l, h)) {
                    candidates += Transaction(l.sets ++ h.sets) // {1} ++ {2} -> {1, 2}
                }
            }
        }
    
        candidates.toList
    }
}
