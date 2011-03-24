package org.school.association.rule

import org.school.core.{ItemSet, Transaction, FrequentSet, RuleSet}

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
        List[RuleSet[T]]()
    }
}
