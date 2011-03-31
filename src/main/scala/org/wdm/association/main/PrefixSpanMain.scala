package org.wdm.association.main

import org.wdm.main.RunnerTrait
import org.wdm.core.{Transaction, FrequentSet}
import org.wdm.core.support.AbstractSupport
import org.wdm.association.PrefixSpan

/**
 * The main runner for the prefix span algorithm 
 */ 
object PrefixSpanMain extends RunnerTrait { 

    val version  = "0.1.0"
    val mainName = "PrefixSpan"

    /**
     * Process the given input data with the prefix span algorithm
     *
     * @param database The dataset to process
     * @param support The support lookup table
     * @return A list of the frequentsets found
     */
    def algorithm[T](database:List[Transaction[T]], support:AbstractSupport[T])
        : List[FrequentSet[T]] = {
    
        val associator = new PrefixSpan(database, support)
        associator.process
    }
}
