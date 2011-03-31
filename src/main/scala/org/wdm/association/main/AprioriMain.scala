package org.wdm.association.main

import org.wdm.main.RunnerTrait
import org.wdm.core.{Transaction, FrequentSet}
import org.wdm.core.support.AbstractSupport
import org.wdm.association.Apriori

/**
 * The main runner for the apriori algorithm 
 */ 
object AprioriMain extends RunnerTrait { 

    val version  = "0.1.0"
    val mainName = "Apriori"

    /**
     * Process the given input data with the apriori algorithm
     *
     * @param database The dataset to process
     * @param support The support lookup table
     * @return A list of the frequentsets found
     */
    def algorithm[T](database:List[Transaction[T]], support:AbstractSupport[T])
        : List[FrequentSet[T]] = {
    
        val associator = new Apriori(database, support)
        associator.process
    }
}
