============================================================
Web Data Mining
============================================================

This is basically my "working through WDM algorithms" repository

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
    * ant
    * ivy (trying to make ant do this for you)

With those dependencies met, this will get you running from
start to finish::

    git clone https://github.com/bashwork/wdm.git
    cd wdm
    ant resolve
    ant package && ./config/runner.sh

============================================================
Notes
============================================================

At the moment, Scala is just a wasteland:

  * The BCL is poluted, inconsistent, and unorganized
  * Everything is a method is cute, but in practice just
    produces more bullshit that requires more linenoise to
    deal with.
  * Working with lists is just awful unless you have a PH.D
    in combinators.

Also, the state of static typing as well as Generics (f***
type erasure) is just kind of embarrassing.

============================================================
License
============================================================

Steal and be happy
