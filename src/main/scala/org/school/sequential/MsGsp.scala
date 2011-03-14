package org.school.sequential

import org.school.core.{Item, ItemSet, Transaction}

class MsGsp(val transactions:List[Transaction]) {

    def process() : List[Transaction] {
        val frequents = List[Transaction]()
        val candidate1 = initialize()
    }

    /**
     * Generate all the unique items in the list of transactions
     * Note: this comprises the following portions of the algorith:
     * * M: sort(I,MS)
     * * L: init-pass(M, S)
     *
     * @return A list of all the unique items
     */
    private def initialize() : List[Item] = {
        val items  = transactions.map { _.candidates }.flatten
        val unique = items.toSeq.toList 
        val sorted = unique.sortWith { (a,b) => a.support < b.support }

        sorted.map { x => ItemSet(x) }
    }

   // private def cadidateGen2() : {

   // }

   // private def cadidateGen() : {

   // }
}
