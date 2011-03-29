package org.wdm.distance

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class ChebyshevDistanceSpec extends FlatSpec with ShouldMatchers {

	behavior of "chebyshev distance"

	it should "evaluate for two numbers" in {
		ChebyshevDistance(1,2) should be (1)
		ChebyshevDistance(2.0,4.0) should be (2.0)
	}

	it should "evaluate for two sets" in {
        val a = List[Double](1,4,6,8)
        val b = List[Double](3,9,2,10)

		ChebyshevDistance(a,b) should be (5.0)
	}
}

