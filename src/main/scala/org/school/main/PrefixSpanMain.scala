package org.school.main

import org.school.core._
import org.school.association.PrefixSpan

/**
 * 
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
