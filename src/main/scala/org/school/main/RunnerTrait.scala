package org.school.main

import org.apache.commons.cli._
import org.slf4j.{Logger, LoggerFactory}

import org.school.core._
import org.school.core.loader.LoaderFactory
import org.school.core.output.SequentialFormatter
import org.school.core.format.SequentialFormat


/**
 * The main launcher script for the service. This parses the following
 * command line options:
 *
 * - h | help    : prints this help text
 * - v | version : prints the version of the server
 * - i | input   : specify the input data to parse
 */
trait RunnerTrait {

    private val logger = LoggerFactory.getLogger(this.getClass)
    val version : String
    val mainName : String

	/**
     * Process the given input data with the given algorithm
     *
     * @param database The dataset to process
     * @param support The support lookup table
     * @return A list of the frequentsets found
     */
	def algorithm[T](database:List[Transaction[T]], support:AbstractSupport[T])
		: List[FrequentSet[T]]

	/**
     * Processes the command line arguments
     *
     * @param options The options to parse
     */
	def process(options: Map[String,Any], error: Unit) {
        implicit def _atos(a:Any) = a.asInstanceOf[String]
        implicit def _atoi(a:Any) = a.toString.toInt

		val sploader = LoaderFactory(options("support")).get
		val support  = MultipleSupport(sploader.load)

		val dbloader = LoaderFactory(options("input")).get
		val database = SequentialFormat.process(dbloader)

		val results  = algorithm(database, support)
		val outputer = new SequentialFormatter(results)  
		outputer.toFile(options("output"))
	}

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
              case "i" | "input"    => defaults += ("input" -> o.getValue())
              case "o" | "output"   => defaults += ("output" -> o.getValue())
              case "s" | "support"  => defaults += ("support" -> o.getValue())
              case "v" | "version"  => printVersion
              case "h" | "help" | _ => printHelp(options)
          }
        }

        process(defaults, () => printHelp(options))
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
        options.addOption("s", "support", true, "specify the support data to parse")
        options.addOption("o", "output", true, "specify the output file")
    }

    /**
     * Helper method to create the default options
     *
     * @return The default options map
     */
    private def createDefaults() = Map[String,Any](
        "input"   -> "data.txt",
        "support" -> "para.txt",
        "output"  -> (mainName.toLowerCase + "-results.txt"))

    /**
     * Helper method to print the current version and exit
     */
    private def printVersion() = {
        println("WDM Version " + version)
        exit
    }

    /**
     * Helper method to print the option help and exit
     */
    private def printHelp(options: Options) = {
        val format = new HelpFormatter()
        format.printHelp("java -jar " + mainName, options)
        exit
    }
}


