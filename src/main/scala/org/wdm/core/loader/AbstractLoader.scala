package org.wdm.core.loader

/**
 * This defines an interface to a loader implementation
 */
trait AbstractLoader {

    /**
     * Loads the underlying collection
     *
     * @return An iterator around the given source
     */
    def load() : Iterator[String]
}

/**
 * This defines an interface to a loader factory implementation
 */
trait AbstractLoaderFactory {

    /**
     * Tests if this loader supports loading the resource
     * at the referenced location.
     *
     * @param location The URI that should be tested
     * @return true if this loader supports this location
     */
    def supports(location:String) : Boolean

    /**
     * Creates a loader for the specified location
     *
     * @param location The URI that should be loaded
     * @return A new loader instance
     */
    def apply(location:String) : AbstractLoader
}
