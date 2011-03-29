package org.wdm.association

import org.slf4j.{Logger, LoggerFactory}
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer
import org.wdm.core.support.AbstractSupport
import org.wdm.core.{ItemSet, Transaction, FrequentSet}

/**
 * This is an implementation of the Minimum Support Prefix Span algorithm
 * outlined in WDM. Given a collection of transactions * and an item
 * support lookup table, it will generate a a listing of frequent
 * transactions as well as their support count.
 *
 * @param sequences The collection of transactions to process
 * @param support The support lookup table for each item
 */
class MsPrefixSpan[T](val sequences:List[Transaction[T]],
    val support:AbstractSupport[T]) extends AbstractAssociation[T] {

    /** This is N represented in the gsp algorithm */
    private val sizeN = sequences.size.doubleValue
    private val logger = LoggerFactory.getLogger(this.getClass)
    /** so that we don't have to pass state around */
    private val frequentDb = HashMap[Int, ListBuffer[Transaction[T]]]()
    /** the actual support for all the items in the dataset */
    private val actual = HashMap[T, Double]()

    /**
     * Processes the current sequence list to produce all the
     * frequent sequences with the supplied support.
     *
     * @return A list of frequent itemsets
     */
    def process() : List[FrequentSet[T]] = {
        stopwatch.start
        val frequent = initialize()
        logger.debug("actuals: ", actual)
        buildFrequents(frequent)

        logger.debug("processing took " + stopwatch.toString)
        frequentDb.map { case(count, buffer) => {
            FrequentSet(buffer.toList) }            // convert map to frequent list
        }.toList.sortWith { _.length < _.length }   // order by frequent length
    }

    /**
     * Generate all the unique items in the list of transactions
     * Note: this comprises the following portions of the algorithm:
     * * M: sort(I,MS)
     * * L: init-pass(M, S)
     *
     * @return A list of all the unique items
     */
    private def initialize() : FrequentSet[T] = {
        val allItems = sequences.map { _.unique }.flatten                           // I with repeats
        val counts   = allItems groupBy identity mapValues { _.size }               // I.count
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

        FrequentSet(filtered.map { x => {                                           // <{F1}>
        val transaction = Transaction(List(ItemSet(x)), counts(x))
            transaction.support = actual(x) // so we don't have to scan again
            addFrequent(1, transaction)     // add frequent items to the database
            transaction
       }})
    }

    /**
     * Given a possible candidate set, search the sequences to see if any
     * of the candidates are frequent.
     *
     * @param candidate The initial candidate to explore
     * @return The frequent candidate list
     */
    private def buildFrequents(candidate:FrequentSet[T]) : List[FrequentSet[T]] = {

        val frequents = ListBuffer[FrequentSet[T]](candidate)

        candidate.transactions.foreach { transaction =>
            val s = initializePotentials(transaction)           // projections
            val count = math.ceil(transaction.minsup(support)   // count(MIS(ik))
                * sizeN).intValue
            val (frequent, sk) = removeInfrequent(s, count)     // local frequents

            frequent.foreach { case(i, icount) => {
                val ik = Transaction(List(ItemSet(i)), icount)
                ik.root = transaction.sets.head
                val result  = restrictedPrefixSpan(ik, sk, count) 
            } }
        }

        logger.debug("built frequents: " + frequents.toList)
        frequents.toList
    }

    /**
     * Helper method to test that two transactions exceed the support
     * difference constraint.
     *
     * @param left The left transaction to test
     * @param right The right transaction to test
     * @return The result of the test
     */
    private def evaluateSdc(left:Transaction[T], right:Transaction[T]) =
        math.abs { left.support - right.support } <= support.sdc

    /**
     * Helper method to test that two items exceed the support
     * difference constraint.
     *
     * @param left The left item to test
     * @param right The right item to test
     * @return The result of the test
     */
    private def evaluateSdc(left:T, right:T) =
        math.abs { actual.getOrElse(left, 0.0) - actual.getOrElse(right, 0.0) } <= support.sdc

    /**
     * Given a frequent item, produce a list of potential projections
     * to be used by the prefix span.
     *
     * @param transaction The transaction to build projections for
     * @return A list of possible projections
     */
    private def initializePotentials(transaction:Transaction[T])
        : List[Transaction[T]] = {
        val potentials = ListBuffer[Transaction[T]]()
        val ik = transaction.sets.head.items.head                       // current frequent item

        sequences.foreach { sequence =>                                 // check every sequence for possible match
            if (sequence contains transaction) {                        // only sequences with ik
                val pruned = sequence.sets.map { set =>                 // remove all items that don't meet
                    val filtered = set.items.filter { item =>           // the minimum support (sdc)
                        evaluateSdc(ik, item) }
                    ItemSet(filtered)                                   // new filtered itemset
                }.filter { _.size > 0 }

                if ((pruned.size > 1) || (pruned.head.size > 1)) {      // eliminate prefix only patterns
                    potentials += Transaction(pruned)
                }
            }
        }

        logger.debug("built potentials: " + potentials.toList)
        potentials.toList
    }

    /**
     * The book says perform this step in a later stage, however it seems
     * easier to just do it before we enter rPrefixSpan.
     *
     * @param s The local database to remove infrequent items from
     * @param mincount The minimum count that must be met
     * @return (frequency-table, Sk-cleaned)
     */
    private def removeInfrequent(s:List[Transaction[T]], mincount:Int)
        : (HashMap[T, Int], List[Transaction[T]]) = {

        val local = HashMap[T, Int]()
        val filtered = ListBuffer[Transaction[T]]()

        s.foreach { sequence =>                 // build our frequency database
            sequence.sets.foreach { set =>
                set.items.foreach { item =>     // for every item in the sequnce
                    local(item) = local.getOrElse(item, 0) + 1
                }
            }
        }

        val frequents = local.filter {          // get our frequency database
            case(k,v) => v >= mincount }

        s.foreach { sequence =>                 // in the local database
            val items = sequence.sets.map { set =>
                ItemSet(set.items.filter {      // remove infrequent values
                    frequents.contains(_) })
            }.filter { _.size > 0 }             // remove empty itemsets
            filtered += Transaction(items)
        }

        (frequents, filtered.toList)            // (support, database)
    }

    /**
     * Performs the recursive prefix span
     *
     * @param ik The transaction to build a projection for
     * @param sk The potentials for this transaction
     * @param mincount The minimum count that must be met
     */
    private def restrictedPrefixSpan(ik:Transaction[T], sk:List[Transaction[T]],
        mincount:Int) {

        val projections = projectDatabase(ik, sk, mincount)
        if (!projections.isEmpty) {
            extendPrefix(projections, ik, sk, mincount)
        }
    }

    /**
     * Builds the projection database for the given transaction
     *
     * @param ik The transaction to build a projection for
     * @param sk The potentials for this transaction
     * @param mincount The minimum count that must be met
     * @return The projection database
     */
    private def projectDatabase(ik:Transaction[T], sk:List[Transaction[T]],
        mincount:Int) : List[Transaction[T]] = {

        val projections = ListBuffer[Transaction[T]]()

        sk.foreach { sequence =>
            sequence.project(ik) match {
                case Some(projection) => projections += projection
                case None =>
            }
        }

        logger.debug("built projections({}): {} ", ik, projections.toList)
        projections.toList
    }

    /**
     * Builds the projection database for the given transaction
     *
     * @param ik The transaction to build a projection for
     * @param sk The potentials for this transaction
     * @param mincount The minimum count that must be met
     */
    private def extendPrefix(projections:List[Transaction[T]],
        ik:Transaction[T], sk:List[Transaction[T]], mincount:Int) {

        val patterns = ListBuffer[Transaction[T]]()

        logger.debug("{} extend {} ", ik, projections.toList)
        projections.foreach { projection =>         // extend our prefix with any possible projections
            projection.sets.foreach { set =>        // extend one item at a time
                set.items.zipWithIndex.foreach {
                    case(_, index) if index == set.templateIndex =>     // skip "_" item
                    case(item, index) => {
                        val ext = if (index > set.templateIndex) {      // Form <{30, x}>
                            Transaction(ik.sets.init ++ List(ItemSet(
                                ik.sets.last.items ++ List(item))))
                        } else {                                        // Form <{30}{x}>
                            Transaction(ik.sets ++ List(ItemSet(item)))
                        }
                        if (!patterns.exists { _ == ext }) { patterns += ext }
                } }
            }
        }

        sk.foreach { sequence =>                    // check the pattern against sk
            patterns.foreach { pattern =>           // build the pattern support count
                if (sequence contains pattern) {
                    pattern.count += 1
                }
            }
        }

        // for each frequent pattern
        patterns.filter { _.count >= mincount }.foreach { pattern =>
            if (pattern contains ik.root) {                 // we must still contain the root
                addFrequent(pattern.length, pattern)        // add the frequent pattern
                restrictedPrefixSpan(pattern, sk, mincount) // find further extensions
            }
        }
    }

    /**
     * A little helper to deal with frequentDb initialization
     *
     * @param count The frequent count to store this under
     * @param transaction The transaction to append
     */
    private def addFrequent(count:Int, transaction:Transaction[T]) {
        val buffer = frequentDb.getOrElseUpdate(count, ListBuffer[Transaction[T]]())
        if (!buffer.exists { _ == transaction }) {          // prevent duplicates
            buffer += transaction
        }
    }
}
