package org.school.core.output

import java.io.FileWriter
import org.school.core.FrequentSet

trait AbstractFormatter {

    /**
     * Processes the given source into ItemSets
     *
     * @return The processed string result
     */
    def format() : String

    /**
     * Stores the processed result to the specified file
     *
     * @param filename The file to store the results in
     */
    def toFile(filename:String) {
        val output = new FileWriter(filename)

        try {
            output.write(format)
        } finally { output.close }
    }

    /**
     * Sends the processed result to console
     */
    def toConsole() { println(format) }
}
