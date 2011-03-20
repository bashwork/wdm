package org.school.main

/**
 * The main launcher script for the service. This parses the following
 * command line options:
 */
object WdmMain {

    /**
     * Main program start
     */
    def main(args: Array[String]) = {
        println("Call this like follows:")
        println("java -cp wdm.jar {algorithm} {parameters}")
        exit
    }
}


