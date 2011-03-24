package org.school.core

import java.io.Serializable

/**
 * Represents a collection of k-length frequent sequence
 * items.
 *
 * @param transactions The k-length frequent patterns
 * @param length The pattern length this represents
 */
class RuleSet[T] private (val predicate:List[Transaction[T]],
    val result:List[Transaction[T]]) extends Serializable {

    /** The size of the rulesets */
    def size() = predicate.size + result.size

    override def hashCode = predicate.hashCode + result.hashCode
    override def equals(other:Any) = other match {
        case that: RuleSet[_] => (that.predicate == this.predicate) &&
                                 (that.result == this.result)
        case _ => false
    }
    override def toString() = {
        predicate.mkString("\n") + " -> " + result.mkString("\n")
    }
}

/**
 * Companion object for the rule class
 */
object RuleSet {
    def apply[T](predicate:List[Transaction[T]], result:List[Transaction[T]]) =
        new RuleSet[T](predicate, result)
}
