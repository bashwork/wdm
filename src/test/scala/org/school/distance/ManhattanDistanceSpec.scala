package org.school.distance

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class ManhattanDistanceSpec extends FlatSpec with ShouldMatchers {

	behavior of "manhattan distance"

	it should "evaluate for two numbers" in {
		ManhattanDistance(1,2) should be (1)
		ManhattanDistance(2.0,4.0) should be (2.0)
	}

	it should "evaluate for two sets" in {
        val a = List[Double](1,4,6,8)
        val b = List[Double](3,9,2,10)

		ManhattanDistance(a,b) should be (13.0)
	}
}

