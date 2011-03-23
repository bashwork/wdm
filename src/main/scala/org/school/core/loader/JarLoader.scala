package org.school.core.loader

import java.util.jar.JarFile

/**
 * A loader that returns an iterator around a file resource
 */
class JarLoader (val location:String) extends AbstractLoader {

    /**
     * Loads the underlying collection
     *
     * @return An iterator around the given source
     */
    override def load() = {
        val file   = location.split("/", 2)
        val jar    = new JarFile(file.head)
        val entry  = jar.getEntry(file.last)
        val stream = jar.getInputStream(entry)

        io.Source.fromInputStream(stream).getLines
    }
}

/**
 * A factory that is used to test and load a jar resource
 *
 * - jar://file.jar/filename.txt
 * - file.jar/filename.txt
 */
object JarLoader extends AbstractLoaderFactory {

    /**
     * Tests if this loader supports loading the resource
     * at the referenced location.
     *
     * @param location The URI that should be tested
     * @return true if this loader supports this location
     */
    override def supports(location:String) =
        location.startsWith("jar://") ||
        location.contains(".jar")

    /**
     * Creates a loader for the specified location
     *
     * @param location The URI that should be loaded
     * @return A new loader instance
     */
    override def apply(location:String) =
        new JarLoader(location.split("://").last)
}
