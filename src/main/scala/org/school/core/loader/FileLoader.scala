package org.school.core.loader

/**
 *
 */
class FileLoader (val location:String) extends AbstractLoader {

    /**
     * Loads the underlying collection
     *
     * @return An iterator around the given source
     */
    override def load() = io.Source.fromFile(location).getLines
}
/**
 *
 */
object FileLoader extends AbstractLoaderFactory {

    /**
     * Tests if this loader supports loading the file
     * at the referenced location.
     *
     * @param location The URI that should be tested
     * @return true if this loader supports this location
     */
    override def supports(location:String) =
        location.startsWith("file://") ||
        !location.contains("://")

    /**
     * Creates a loader for the specified location
     *
     * @param location The URI that should be loaded
     * @return A new loader instance
     */
    override def apply(location:String) = new FileLoader(location)
}
