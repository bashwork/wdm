package org.school.main

import org.school.core.{Transaction, FrequentSet}
import org.school.core.support.AbstractSupport
import org.school.association.MsApriori

/**
 * The main runner for the ms-apriori algorithm
 */ 
object MsAprioriMain extends RunnerTrait { 

    val version  = "0.1.0"
    val mainName = "MsApriori"

    /**
     * Process the given input data with the ms apriori algorithm
     *
     * @param database The dataset to process
     * @param support The support lookup table
     * @return A list of the frequentsets found
     */
    def algorithm[T](database:List[Transaction[T]], support:AbstractSupport[T])
        : List[FrequentSet[T]] = {
    
        val associator = new MsApriori(database, support)
        associator.process
    }
}
