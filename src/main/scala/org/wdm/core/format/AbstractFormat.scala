package org.wdm.core.format

import org.wdm.core.Transaction
import org.wdm.core.loader.AbstractLoader

/**
 */
trait AbstractFormat {

    /**
     * Processes the given source into ItemSets
     *
     * @param source The source to be processed
     * @param lookup The support lookup table
     * @return The processed list iterator
     */
    def process(source:Iterator[String]) : List[Transaction[String]]
    def process(source:AbstractLoader) : List[Transaction[String]] = {
        process(source.load)
    }
}
