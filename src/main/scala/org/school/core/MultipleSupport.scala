package org.school.core

import scala.collection.mutable.HashMap
import org.slf4j.{Logger, LoggerFactory}
import org.school.core.loader.LoaderFactory

/**
 * Implements a support lookup that has multiple support values
 */
class MultipleSupport[T] private (val table:Map[T,Double],
	val default:Double = 0.0) extends AbstractSupport[T] {

    /**
     * Retrieves the requested value for the specified key
     *
     * @param key The key to find the value for
     * @return the value for the specified input
     */
    override def get(key:T) = table getOrElse(key, default)
}

object MultipleSupport {

	private val logger = LoggerFactory.getLogger(this.getClass)
    private val misMatcher = """MIS\((\d+)\)\s=\s(.+)""".r
    private val sdcMatcher = """SDC\s=\s(.+)""".r

    def apply[T](lookup:Map[T,Double]) = new MultipleSupport[T](lookup)
    def apply(source:Iterator[String]) : MultipleSupport[String] = {
		var sdc = 0.0
		var data = Map[String, Double]()

        source.foreach { line => line match {
            	case misMatcher(value, mis) => data += value -> mis.toDouble
            	case sdcMatcher(value) => sdc = value.toDouble
				case _ => logger.debug("Invalid support value: " + line)
			}
        }

        val support = new MultipleSupport[String](data)
		support.sdc = sdc
		support
    }
}
