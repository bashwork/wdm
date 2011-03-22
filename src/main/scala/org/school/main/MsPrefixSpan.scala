package org.school.main

import org.school.core._
import org.school.association.MsPrefixSpan

/**
 * 
 */ 
object MsPrefixSpanMain extends RunnerTrait { 

    val version  = "0.1.0"
    val mainName = "MsPrefixSpan"

    /**
     * Process the given input data with the prefix span algorithm
     *
     * @param database The dataset to process
     * @param support The support lookup table
     * @return A list of the frequentsets found
     */
    def algorithm[T](database:List[Transaction[T]], support:AbstractSupport[T])
        : List[FrequentSet[T]] = {
    
        val associator = new MsPrefixSpan(database, support)
        associator.process
    }
}
