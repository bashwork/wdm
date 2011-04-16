package org.wdm.core

import java.io.Serializable
import scala.collection.mutable.ListBuffer
import org.wdm.core.support.AbstractSupport

/**
 * Represents an ordered sequence of itemsets.
 * Note that count is only used for the frequency sets.
 *
 * @param sets The itemsets composing this Transaction
 * @param count Used to indicate the frequency of this transaction
 * @param restCount A count used during rule generation
 */
class Transaction[T] private (val sets:List[ItemSet[T]],
    var count:Int, var restCount:Int) extends Serializable {

    /** The current support of this transaction */
    var support = 0.0

    /** Every prefix span extension should contain this */
    var root = ItemSet[T]()

    /** The lowest minimum support allowable */
    var minMisItem = ItemSet[T]()

    /** The number of itemsets in this transaction */
    def size()     = sets.size

    /** The total number of items in all the itemsets */
    def length()   = sets.foldLeft(0) { (t,s) => t + s.size }

    /** A flat list of all the unique items in this transaction */
    def unique()   = sets.map { _.items }.flatten.toSet

    /** A flat list of every item in this transaction */
    def allItems() = sets.map { _.items }.flatten.toList

    /**
     * Checks if the supplied transaction is a contiguous
     * subset of this transaction.
     *
     * @param other The other transaction to test
     * @return true if successful, false otherwise
     */
    def contains(other:Transaction[T]) : Boolean = {
        var index = -1                      // set intially not found

        other.sets.foreach { o =>           // apply sliding window scan
            val next = sets.drop(index + 1).findIndexOf { t =>
                t contains o } + index + 1  // add index back
            if (next <= index) { return false }
            index = next;
        }

        true;
    }

    /**
     * Checks if the supplied transaction is a contiguous
     * subset of this transaction.
     *
     * @param other The other transaction to test
     * @return true if successful, false otherwise
     */
    def contains(other:ItemSet[T]) =
        sets.exists { set => set contains other }

    /**
     * Remove an the item to the left or right of the list (max 2)
     *
     * @param support The support lookup table
     * @return The minimum support for this collection
     */
    def project(pattern:Transaction[T]) : Option[Transaction[T]] = {
        if (!contains(pattern)) { return None }

        // since we know we are contained, just find the last index of our last
        // element and we can drop the beggining of the set
        val index = sets findIndexOf { _ contains pattern.sets.last }

        val form1 = Transaction(sets.drop(index).filterNot {        // attempt {ik}{x} match
            pattern.sets contains _ })                              // prune all {ik} matches

        if (!form1.contains(pattern.sets.last)) {                   // if we do not contain ik
            return if (form1.length > 0) Some(form1)                // then we are a {ik}{x} match
            else None                                               // skip empty projections
        }

        form1.sets.findIndexOf { set =>                             // otherwise we are a {ik, x} match
            val index = set.items.findIndexOf {                     // find which set has our match
                _ == pattern.sets.last.items.last } 
            if (index >= 0) { set.templateIndex = index }           // set that index to _
            (index >= 0)
        }
        if (form1.length > 0) Some(form1) else None                 // skip empty projections
    }
    

    /**
     * Remove an the item to the left or right of the list (max 2)
     *
     * @param support The support lookup table
     * @return The minimum support for this collection
     */
    def without(index:Int) : Transaction[T] = {
        val id = index match {
            case i if index >= 0 => {
                if (sets.head.size >= i) (0, i)
                else (1, i - sets.head.size)
            }
            case _ if index  < 0 => {
                val i = math.abs(index)
                if (sets.last.size >= i) (sets.size  - 1, sets.last.size - i)
                else (sets.size - 2, (sets(sets.size - 2).size) - (i - sets.last.size))
            }
        }

        val (l, (m :: r)) = sets.splitAt(id._1)
        val middle = if (m.size == 1) List[ItemSet[T]]()
            else List(ItemSet(m.items.filterNot { m.items.indexOf(_) == id._2 }))
        Transaction(l ++ middle ++ r)
    }

    /**
     * Retrieve the minimum support for this transaction set
     *
     * @param support The support lookup table
     * @return The minimum support for this collection
     */
    def minsup(support:AbstractSupport[T]) =
        sets.map { s => s.minsup(support) }.min

    /**
     * Performs the join specified in GSP
     * To test if we can join, we perform the following:
     * 1. Is the first element of the join list the same
     * 2. Is the first element of the join list the same
     *
     * @param right The other transaction to join
     * @return The new joined transaction
     */
    def join(right:Transaction[T]) : Option[Transaction[T]] = {
        val jleft = if (sets.head.size == 1) sets.tail
            else ItemSet(sets.head.items.tail) :: sets.tail
        val jright = if (right.sets.last.size == 1) right.sets.init
            else right.sets.init ++ List(ItemSet(right.sets.last.items.init))
        
        def joiner = {
            val (r, l) = (right.sets.last, sets.last)
            val post = if (r contains l) List(r)            // {3}    & {3, 4}
                else if (l.items.last == r.items.head)      // {3, 4} & {4, 5}
                    List(ItemSet(l.items :+ r.items.last))
                else List(l, r)                             // {3}    & {4}
            
            Transaction(sets.init ++ post)
        }
        
        if (jleft == jright) Some(joiner) else None
    }

    /**
     * Returns all the subsequence permutations
     *
     * @return The list of subsequence permutations
     */
    def subsequences : List[Transaction[T]] = {
        val possible = ListBuffer[Transaction[T]]()

        for (i <- 0 to sets.size - 1) {
            val (left, middle :: right) = sets.splitAt(i)
            for (j <- 0 to middle.size - 1) {
                val (il, im :: ir) = middle.items.splitAt(j)
                val set = if ((il.size + ir.size) != 0)
                    List(ItemSet(il ++ ir)) else List[ItemSet[T]]()
                possible += Transaction(left ++ set ++ right)
            }
        }

        possible.toList
    }

    override def hashCode = sets.hashCode
    override def equals(other:Any) = other match {
        case that: Transaction[_] => that.sets == this.sets
        case _ => false
    }

    override def toString() = sets.mkString("<", "", ">")
}

/**
 * Companion object for the transaction class
 */
object Transaction {
    def apply[T](items:ItemSet[T]*) = new Transaction[T](items.toList, 0, 0)
    def apply[T](items:List[ItemSet[T]], count:Int = 0) = new Transaction[T](items, count, 0)
    def apply[T](copy:Transaction[T]) = {
        val transaction = new Transaction[T](copy.sets, copy.count, copy.restCount)
        transaction.support = copy.support
        transaction.minMisItem = copy.minMisItem
        transaction
    }
}
