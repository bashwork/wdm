package org.wdm.association.rule

import scala.collection.mutable.ListBuffer
import org.wdm.core.{ItemSet, Transaction, FrequentSet, RuleSet}

/**
 * Used to generate all the association rules given a frequent
 * item collection and a minimum confidence threshold.
 */
class AssociationRule[T](val frequents:List[FrequentSet[T]],
    val minconf:Double) {

    /**
     * Processes the current list of frequent itemsets
     * to generate valid association rules.
     *
     * @return A list of valid association rules
     */
    def process() : List[RuleSet[T]] = {
        val rules = ListBuffer[RuleSet[T]]()

        frequents.foreach { frequent =>
            // generate each rule
        }
        
        rules.toList
    }
}
