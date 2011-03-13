package org.school.association

import org.apache.commons.cli._
import org.slf4j.{Logger, LoggerFactory}

import org.school.core.loader.LoaderFactory


/**
 * The main launcher script for the service. This parses the following
 * command line options:
 *
 * - h | help    : prints this help text
 * - v | version : prints the version of the server
 * - i | input   : specify the input data to parse
 * - d | debug   : turn on the internal debuggging
 */
trait RunnerTrait {

    val Version = "1.0.0"
    private val logger = LoggerFactory.getLogger(this.getClass)

    /**
     * Main program start
     */
    def main(args: Array[String]) = {
        var defaults = createDefaults
        val options  = createOptions
        val parser   = new PosixParser()
        val results  = parser.parse(options, args)

        results.getOptions.foreach { o:Option =>
          o.getOpt match {
              case "d" | "debug"    => defaults += ("debug" -> true)
              case "i" | "input"    => defaults += ("input" -> o.getValue())
              case "v" | "version"  => printVersion
              case "h" | "help" | _ => printHelp(options)
          }
        }
        implicit def _atos(a:Any) = a.asInstanceOf[String]
        implicit def _atoi(a:Any) = a.toString.toInt

        // algorithm implementation
    }

    /**
     * Helper method to create the option parser set
     *
     * @return The populated option parser set
     */
    private def createOptions() : Options = {
        val options = new Options()
        options.addOption("h", "help", false, "print this help text")
        options.addOption("v", "version", false, "print the version of the server")
        options.addOption("i", "input", true, "specify the input data to parse")
        options.addOption("d", "debug", true, "turn on the internal debuggging")
    }

    /**
     * Helper method to create the default options
     *
     * @return The default options map
     */
    private def createDefaults() = Map[String,Any](
        "debug" -> false,
        "input" -> null)

    /**
     * Helper method to print the current version and exit
     */
    private def printVersion() = {
        println("WDM Version " + "1.0")
        exit
    }

    def mainName() : String

    /**
     * Helper method to print the option help and exit
     */
    private def printHelp(options: Options) = {
        val format = new HelpFormatter()
        format.printHelp("java -jar " + mainName, options)
        exit
    }
}


