package org.wdm.distance

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class EuclideanDistanceSpec extends FlatSpec with ShouldMatchers {

	behavior of "euclidean distance"

	it should "evaluate for two numbers" in {
		EuclideanDistance(1,2) should be (1)
		EuclideanDistance(2.0,4.0) should be (2.0)
	}

	it should "evaluate for two sets" in {
        val a = List[Double](1,4,6,8)
        val b = List[Double](3,9,2,10)

		EuclideanDistance(a,b) should be (7.0)
	}
}

