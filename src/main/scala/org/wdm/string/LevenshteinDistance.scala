package org.wdm.string

/**
 * Implements the levenshtein distance algorithm to
 * compare the distance between to given strings
 */
object LevenshteinDistance {

    /**
     * @param left The left string to compare
     * @param right The right string to compare
     * @return Teh distance between the two strings
     */
    def apply(left:String, right:String) : Int = {
        val (llen, rlen) = (left.length, right.length)
        val dist = Array.ofDim[Int](llen + 1, rlen + 1)

        for (i <- 0 to llen) dist(i)(0) = i
        for (j <- 0 to rlen) dist(0)(j) = j
        for (j <- 1 to rlen; i <- 1 to llen) {
            val cost = if (left(i - 1) == right(j - 1)) 0 else 1
            dist(i)(j)  = minimum(
                dist(i - 1)(j) + 1,         // a deletion
                dist(i)(j - 1) + 1,         // an insertion
                dist(i - 1)(j  - 1) + cost) // a substitution
        }

        dist(llen)(rlen)
    }

    /** A helper method to get the minimum of 3 values */
    private def minimum(a:Int, b:Int, c:Int) : Int =
        math.min(math.min(a,b), c)
}
