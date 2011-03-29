package org.wdm.main

import org.wdm.core.{Transaction, FrequentSet}
import org.wdm.core.support.AbstractSupport
import org.wdm.association.GeneralizedSequentialPattern

/**
 * The main runner for the ms-gsp algorithm 
 */ 
object GeneralizedSequentialPatternMain extends RunnerTrait { 

    val version  = "0.1.0"
    val mainName = "GeneralizedSequentialPattern"

    /**
     * Process the given input data with the GSP algorithm
     *
     * @param database The dataset to process
     * @param support The support lookup table
     * @return A list of the frequentsets found
     */
    def algorithm[T](database:List[Transaction[T]], support:AbstractSupport[T])
        : List[FrequentSet[T]] = {

        val associator = new GeneralizedSequentialPattern(database, support)
        associator.process
    }
}
