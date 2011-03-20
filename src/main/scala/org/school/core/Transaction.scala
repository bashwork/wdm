package org.school.core

import java.io.Serializable
import scala.collection.mutable.ListBuffer

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
        val initial = other.sets.map { o =>
            sets.findIndexOf { t => t contains o } }
        var result = !initial.contains(-1) && !initial.isEmpty
        for (i <- 0 until initial.size -1) {
            result &= initial(i) < initial(i + 1)
        }

        result
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
			val post = if (r contains l) List(r)	            // {3}    & {3, 4}
				else if (l.items.last == r.items.head)			// {3, 4} & {4, 5}
					List(ItemSet(l.items ++ List(r.items.last)))
				else List(l, r)					                // {3}    & {4}
			
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

object Transaction {
    def apply[T](items:ItemSet[T]*) = new Transaction[T](items.toList, 0, 0)
    def apply[T](items:List[ItemSet[T]], count:Int = 0) = new Transaction[T](items, count, 0)
}
