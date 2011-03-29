package org.wdm.core.loader

import java.util.zip.ZipFile

/**
 * A loader that returns an iterator around a file resource
 */
class ZipLoader (val location:String) extends AbstractLoader {

    /**
     * Loads the underlying collection
     *
     * @return An iterator around the given source
     */
    override def load() = {
        val file   = location.split("/", 2)
        val zip    = new ZipFile(file.head)
        val entry  = zip.getEntry(file.last)
        val stream = zip.getInputStream(entry)

        io.Source.fromInputStream(stream).getLines
    }
}

/**
 * A factory that is used to test and load a zip resource
 *
 * - zip://file.zip/filename.txt
 * - file.zip/filename.txt
 */
object ZipLoader extends AbstractLoaderFactory {

    /**
     * Tests if this loader supports loading the resource
     * at the referenced location.
     *
     * @param location The URI that should be tested
     * @return true if this loader supports this location
     */
    override def supports(location:String) =
        location.startsWith("zip://") ||
        location.contains(".zip")     ||
        location.contains(".gzip")

    /**
     * Creates a loader for the specified location
     *
     * @param location The URI that should be loaded
     * @return A new loader instance
     */
    override def apply(location:String) =
        new ZipLoader(location.split("://").last)
}
