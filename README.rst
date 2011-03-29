============================================================
Web Data Mining
============================================================

This is basically my "working through WDM algorithms"
repository. As of right now it contains implementations of 
ms-gsp and ms-prefix-span.

============================================================
Structures
============================================================

The following is a list of data structures used throughout the
code in case you get lost::

    Name         How to Get              Actual Type
    ---------------------------------------------------------
    Frequents == <root>                : List[FrequentSet[T]]
    Frequent  == Frequents.head        : FrequentSet[T]
    Sequences == Frequent.transactions : List[Transaction[T]]
    Sequence  == Sequences.head        : Transaction[T]
    Sets      == Sequence.sets         : List[ItemSets[T]]
    Set       == Sets.head             : ItemSets[T]
    Items     == Set.items             : List[T]
    Item      == Set.items.head        : T

============================================================
Building
============================================================

In order to build wdm, you will need the following::

    * some jdk (your pick, but tested on Oracle's...)
    * git (to pull down the source, or just download)
    * ant (to run the build files)

With those dependencies met, this will get you running from
start to finish::

    git clone https://github.com/bashwork/wdm.git
    cd wdm
    ant resolve
    ant package && ./config/runner.sh

============================================================
Collaboration
============================================================

For this project, I worked alone, however I did compare
results with Mandi Reis as we really had no metric to see
if our programs were working correctly. We did not code
together or share code.

============================================================
Todo
============================================================

Add more datasets and testing for validation (plus a bit
of cleanup and correct functional reworks).
