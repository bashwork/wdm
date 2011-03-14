package org.school.association

import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer
import org.school.core.{AbstractSupport}
import org.school.core.{ItemSet, Transaction}

/**
 *
 * @param sequences The collection of transactions to process
 * @param support The support lookup table for each item
 */
class PrefixSpan[T](val sequences:List[Transaction[T]],
    val support:AbstractSupport[T]) {

    /**
     * Processes the current sequence list to produce all the
     * frequent sequences with the supplied support.
     *
     * @return A list of frequent itemsets
     */
    def process() : List[Transaction[T]] = {
        val frequents = ListBuffer[Transaction[T]]()
        frequents += initialize()

        (1 to 1000).takeWhile( _ => frequents.last.sets.size > 0) foreach { k =>
            val ck = k match {
                case 2 => candidateGen2(frequents.last)
                case _ => candidateGen(frequents.last)
            }
            frequents += buildFrequent(ck)
        }

        frequents.dropRight(1).toList
    }

    /**
     * Generate all the unique items in the list of transactions
     * Note: this comprises the following portions of the algorith:
     * * M: sort(I,MS)
     * * L: init-pass(M, S)
     *
     * @return A list of all the unique items
     */
    private def initialize() : Transaction[T] = {
        val total    = sequences.size                                               // S.sze == n
        val allItems = sequences.map { _.allItems }.flatten                         // I with repeats
        val counts   = allItems groupBy identity mapValues { _.size }               // I.count
        val unique   = counts.keys.toList                                           // I
        val sorted   = unique.sortWith { (a,b) => support.get(a) < support.get(b) } // L
        val filtered = sorted.filter { i => (counts(i) / total) >= support.get(i) } // <F1>

        Transaction(filtered.map { x => ItemSet(x) })                               // <{F1}>
    }

    /**
     * Given a possible candidate set, search the sequences to see if any
     * of the candidates are frequent
     *
     * @param candidates A collection of possible candidates
     * @return The frequent candidate list
     */
    private def buildFrequent(candidates:Transaction[T]) : Transaction[T] = {
        val total  = sequences.size                         // S.sze == n
        val counts = HashMap[ItemSet[T], Int]()

        sequences.map { _.sets }.flatten.foreach { sequence =>
            candidates.sets.foreach { candidate =>
                if (sequence == candidate) {
                    counts.get(candidate) match {
                        case None => counts.put(candidate, 0)
                        case Some(_) => counts(candidate) += 1
                    }
                }
                // two other cases
            }
        }

        val filtered = candidates.sets.filter { c =>
            (counts(c) / total) >= c.minsup(support) }
        Transaction(filtered) // <{Fn}>
    }
    private var default: T = _

    /**
     *
     * @param frequent The previous frequent items to build with
     * @return A possible candidate set to process
     */
    private def candidateGen2(frequent:Transaction[T]) : Transaction[T] = {
    
        Transaction(ItemSet(default))
    }
    
    /**
     * @param frequent The previous frequent items to build with
     * @return A possible candidate set to process
     */
    private def candidateGen(frequent:Transaction[T]) : Transaction[T] = {

        Transaction(ItemSet(default))
    }
}
