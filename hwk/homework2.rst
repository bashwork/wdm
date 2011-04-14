============================================================
Homework 2: Clustering
============================================================

Problem 1: Nearest Neighbors
------------------------------------------------------------

For problems one and two, I actually got the same class labeling when I used
Euclidean and Manhattan distance functions and 5-nearest-neighbors::

    * entry1: "-"
    * entry2: "-"
    * entry3: "-"

The difference between the two methods (1-nn vs 5-nn) is simply the value of K which
instructs the algorithm of how many of the nearest neighbors to use in evaluating
the class label that it should apply to the new data entry.

As for data points with missing values, there are a number of techniques that can
be employed to fill in the attribute values, however, the best way to populate
them without adding too much skew would be to perform k-nearest-neighbors while
ignoring that attribute in the distance evaluation. Then, the value can be set
to the average value of its K neighbors.


Problem 2: Decision Tree
------------------------------------------------------------

For the root node decision on my decision tree, I decided to use the temperature
attribute split at the value of 8.0.  I chose this value because in my calculations
that attribute with that value provided the greatest information gain at that point
int the tree.  The remaining nodes were chosen in the same way.

My first three training examples all evaluated correctly. This was performed
by simply running them through the tree and then assigning them the class of the
leaf node that they finished on::

    * entry1: "-"
    * entry2: "-"
    * entry3: "-"

My accuracy (using the supplied testing dataset) evaluated to 85% before any form
of pruning was utilized. This was calculated by simply running every entry in the
testing dataset through my tree and then counting how many entries were classified
correctly and dividing by the total test set. After looking at the testing dataset,
I don't believe that there is any way I could have pruned the tree to increase its
accuracy as the training set had 100% accuracy and the two classification errors
in the testing set occurred very early in the tree. That being said, I could remove
the entire tree after the condition *light > 8.10* and achieve the same accuracy
as using the entire tree.

To create rule sets out of my tree, I simply perform a depth first walk through
the tree until I reach a leaf. During the walk, each predicate is recorded as a
portion of the rule.  An example rule that was generated is as follows::

   temperature >  8.000000, light <= 8.100000 -> classify(-) 

    if entry.temperature > 8.00:
        if entry.light <= 8.10:
            entry.label = "-"

###TODO rule pruning###

The tree can also be used to infer missing data of new entries. If the entry
can be classified with the tree (i.e. the missing values were not needed to
make a decision) then the missing value(s) can be set to the average value
of all the other entries that reached that node. The problem occurs when the
example is unable to proceed in the tree as the missing value is needed to
make a branching decision.  In this case a decision tree can actually be
built on a training set so that instead of the leafs classifying the new
entry, they will instead deliver the most probably value for the given
attribute. These trees would have to be generated for each attribute that
one would hope to infer from a training set.


Problem 2.1: Generated Decision Tree
------------------------------------------------------------

*The following decision tree was deduced from the training dataset*::

  .. image:: images/training.png


Problem 2.2: Generated Rules
------------------------------------------------------------

*The following rules were deduced from the tree representation*::

    temp <= 8.000000 -> -
    temp >  8.000000, light <= 8.100000 -> -
    temp >  8.000000, light >  8.100000, cloud <= 50.000000, humid <= 96.000000, cloud <= 40.000000 -> +
    temp >  8.000000, light >  8.100000, cloud <= 50.000000, humid <= 96.000000, cloud >  40.000000 -> -
    temp >  8.000000, light >  8.100000, cloud <= 50.000000, humid >  96.000000 -> -
    temp >  8.000000, light >  8.100000, cloud >  50.000000 -> +


Problem 3: Naive Bayes Classifier
------------------------------------------------------------

In order to to use the Bayes classifier on our continuous dataset,
our attribute values must first be discretized into range bins. For this
purpose, I manually chose bin ranges that seemed to spread evenly across
the attibute ranges (i.e. an even number of values were stored in each range).
What follows are the calculations I came up with::

    Global
    --------------------------------------------------------
    size(dataset)       = 23
    P(C = +)            = 12/23    P(C = - )         = 11/23
    
    Temperature: 
    --------------------------------------------------------
    P(T=   -5..0|C = +) = 0/12     P(T=  -5..0|C = -) = 0/12 
    P(T=    1..5|C = +) = 0/12     P(T=   1..5|C = -) = 4/11
    P(T=   6..10|C = +) = 1/12     P(T=  6..10|C = -) = 3/11
    P(T=  11..15|C = +) = 5/12     P(T= 11..15|C = -) = 3/11
    P(T=  16..21|C = +) = 6/12     P(T= 16..21|C = -) = 1/11
    
    Humidity:
    --------------------------------------------------------
    P(H=     80<|C = +) = 1/12    P(H=     80<|C = -) = 2/12 
    P(H=  80..90|C = +) = 5/12    P(H=  80..90|C = -) = 4/12 
    P(H=     >90|C = +) = 6/12    P(H=     >90|C = -) = 5/12 
    
    Light:
    --------------------------------------------------------
    P(L=    0..5|C = +) = 0/12    P(L=    0..5|C = -) = 1/12 
    P(L=   6..10|C = +) = 2/12    P(L=   6..10|C = -) = 8/12 
    P(L=     >10|C = +) = 10/12   P(L=     >10|C = -) = 2/12 
    
    Cloud:
    --------------------------------------------------------
    P(C=   0..25|C = +) = 1/12    P(C=   0..25|C = -) = 1/12 
    P(C=  26..50|C = +) = 2/12    P(C=  26..50|C = -) = 4/12 
    P(C=  51..75|C = +) = 1/12    P(C=  51..75|C = -) = 1/12 
    P(C= 76..100|C = +) = 8/12    P(C= 76..100|C = -) = 5/12 

The problem then asked use to label the entry at 1/24/1988 using
the naive Bayes classifier. The following is the calculation
used to arrive at the "-" label for the entry::

    entry(1/24/1988) = { T:6, H:73, L:9.5, C:30 }

    P(C = +|D=entry) = (12/23)(1/12)(1/12)(2/12)(2/12) = 1.0e-4
    P(C = -|D=entry) = (11/23)(3/11)(2/11)(8/11)(4/11) = 6.27e-3

Problem 4: MAP Hypothesis
------------------------------------------------------------

The MAP hypothesis basically lets us say::

   hmap = max { P(D|H)P(h) }


Problem 5: ML Hypothesis
------------------------------------------------------------

The ML hypothesis basically lets us say::

   hml = max { P(D|H) }

Notes
------------------------------------------------------------

* The code used to predict problems 1 and 2 can be found at:
  https://github.com/bashwork/common/raw/master/python/algorithms/k-nearest-neighbor.py

