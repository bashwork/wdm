package org.school.core.loader

/**
 * This loads an example string into a string iterator
 */
class MemoryLoader (val example:String) extends AbstractLoader {

    /**
     * Loads the underlying collection
     *
     * @return An iterator around the given source
     */
    override def load() = io.Source.fromString(example).getLines
}
/**
 *
 */
object MemoryLoader extends AbstractLoaderFactory {

    /**
     * Tests if this loader supports loading the file
     * at the referenced location.
     *
     * @param location The URI that should be tested
     * @return true if this loader supports this location
     */
    override def supports(location:String) = false

    /**
     * Creates a loader for the specified location
     *
     * @param example The example string to use as input
     * @return A new loader instance
     */
    override def apply(example:String) = new MemoryLoader(example)
}
