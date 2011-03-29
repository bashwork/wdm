package org.wdm.utility

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class StopwatchSpec extends FlatSpec with ShouldMatchers {

	behavior of "A stopwatch"

	it should "initialize and start" in {
		val watch = Stopwatch.startNew

        watch.stop should not be(0L)
	}

	it should "start and stop" in {
		val watch = new Stopwatch()

        watch.elapsed should be(0L)
        watch.toString should be("0.0s")
        watch.start
        Thread.sleep(100)
        watch.stop
        watch.elapsed should not be(0L)
        watch.restart
        watch.elapsed should be(0L)
	}
}

