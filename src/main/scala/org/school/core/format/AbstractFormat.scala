package org.school.core.format

import org.school.core.{Transaction,AbstractSupport}

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
    def process(source:Iterator[String], lookup:AbstractSupport) : List[Transaction]
}
