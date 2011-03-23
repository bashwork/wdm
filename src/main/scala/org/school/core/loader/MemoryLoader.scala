package org.school.core.loader

/**
 * A loader that returns an iterator around a string resource
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
 * A factory that is used to test and load a string resource
 */
object MemoryLoader extends AbstractLoaderFactory {

    /**
     * Tests if this loader supports loading the specified
     * resource. Note, this will always return false as we
     * should only use this loader in tests.
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
