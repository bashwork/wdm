package org.wdm.string

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class LevenshteinDistanceSpec extends FlatSpec with ShouldMatchers {

	behavior of "the levenshtein distance algorithm"

    it should "work on empty strings" in {
        LevenshteinDistance(   "",    "") should be (0)
        LevenshteinDistance(  "a",    "") should be (1)
        LevenshteinDistance(   "",   "a") should be (1)
        LevenshteinDistance("abc",    "") should be (3)
        LevenshteinDistance(   "", "abc") should be (3)
    }

    it should " workon equal strings" in {
        LevenshteinDistance(   "",    "") should be (0)
        LevenshteinDistance(  "a",   "a") should be (0)
        LevenshteinDistance("abc", "abc") should be (0)
    }

    it should "work where only inserts are needed" in {
        LevenshteinDistance(   "",   "a") should be (1)
        LevenshteinDistance(  "a",  "ab") should be (1)
        LevenshteinDistance(  "b",  "ab") should be (1)
        LevenshteinDistance( "ac", "abc") should be (1)
        LevenshteinDistance("abcdefg", "xabxcdxxefxgx") should be (6)
    }

    it should "work where only deletes are needed" in {
        LevenshteinDistance(  "a",    "") should be (1)
        LevenshteinDistance( "ab",   "a") should be (1)
        LevenshteinDistance( "ab",   "b") should be (1)
        LevenshteinDistance("abc",  "ac") should be (1)
        LevenshteinDistance("xabxcdxxefxgx", "abcdefg") should be (6)
    }

    it should "work where only substitutions are needed" in {
        LevenshteinDistance(  "a",   "b") should be (1)
        LevenshteinDistance( "ab",  "ac") should be (1)
        LevenshteinDistance( "ac",  "bc") should be (1)
        LevenshteinDistance("abc", "axc") should be (1)
        LevenshteinDistance("xabxcdxxefxgx", "1ab2cd34ef5g6") should be (6)
    }

    it should "work where many operations are needed" in {
        LevenshteinDistance("example", "samples")              should be (3)
        LevenshteinDistance("sturgeon", "urgently")            should be (6)
        LevenshteinDistance("levenshtein", "frankenstein")     should be (6)
        LevenshteinDistance("distance", "difference")          should be (5)
        LevenshteinDistance("java was neat", "scala is great") should be (7)
    }
}

