package org.school.core

import java.io.Serializable

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
     *
     * @param right The other transaction to join
     * @return The new joined transaction
     */
	def join(right:Transaction[T]) : Option[Transaction[T]] = {
		val left = sets.takeRight(sets.size - 1)
		val isJoinable = (firstSkip contains(right.sets.head)) &&
			(left == right.sets.slice(1, right.sets.size - 1))

		if (isJoinable) Some(lastJoin(left, right.sets.last)) else None
	}

    /**
     * Returns all the subsequence permutations
     *
     * @return The list of subsequence permutations
     */
	def subsequences : List[Transaction[T]] = {
		List(this)
	}

    override def hashCode = sets.hashCode
    override def equals(other:Any) = other match {
        case that: Transaction[_] => that.sets == this.sets
        case _ => false
    }

	override def toString() = sets.mkString("<", "", ">")

	/**
     * Helper method to return the correct first set for join testing
     *
     * @returns The correct first itemset
     */
	private def firstSkip : ItemSet[T] = {
		if (sets.head.size == 1) sets(1)
		else ItemSet(sets.head.items.takeRight(sets.head.size - 1))
	}

	/**
     * Helper method to return the correct last set for joining
     *
     * @param right The other transaction to join
     * @returns The correct last itemset
     */
	private def lastJoin(left:List[ItemSet[T]], right:ItemSet[T]) = {
		val last = sets.last
		val post = if (right contains last) List(right) else List(last, right)
		
		Transaction(left ++ post)
	}
}

object Transaction {
    def apply[T](items:ItemSet[T]*) = new Transaction[T](items.toList, 0, 0)
    def apply[T](items:List[ItemSet[T]], count:Int = 0) = new Transaction[T](items, count, 0)
}
