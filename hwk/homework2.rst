============================================================
Homework 2: Clustering
============================================================

Problem 1: Nearest Neighbors
------------------------------------------------------------

*The code used to predict problems 1 and 2 can be found at:
https://github.com/bashwork/common/raw/master/python/algorithms/k-nearest-neighbor.py*

For problems one and two, I actually got the same class labeling (all "-") when I used
Euclidean and Manhattan distance functions. However, when I changed to using the
Chebyshev distance, the 5-nearest-neighbors returned the following label set:
["+", "+", "-"].

The difference between the two methods (1-nn vs 5-nn) is simply the value of K which
instructs the algorithm of how many of the nearest neighbors to use in evaluating
the class label that it should apply to the new data entry.

As for data points with missing values, there are a number of techniques that can
be employed to fill in the attribute values, however, the best way to populate
them without adding too much skew would be to perform k-nearest-neighbors while
ignoring that attribute in the distance evaluation. Then, the value can be set
to the average value of its K neighbors.


Problem 2: Descision Tree
------------------------------------------------------------

For the root node decision on my decision tree, I decided to use the temperature
attribute split at the value of 8.0.  I chose this value because in my calculations
that attribute with that value provided the greatest information gain at that point
int the tree.  The remaining nodes were chosen in the same way.

My first three training examples all evaluated correctly to "-". This was peformed
by simply running them through the tree and then assigning them the class of the
leaf node that they finished on.

My accurracy (using the supplied testing dataset) evaluated to 85% before any form
of pruning was utilized. This was calculated by simply running every entry in the
testing dataset through my tree and then counting how many entries were classified
correctly and dividing by the total test set. ###TODO###.

To create rule sets out of my tree, I simply perform a depth first walk through
the tree until I reach a leaf. During the walk, each predicate is recorded as a
portion of the rule.  An example rule that was generated is as follows:

   temperature >  8.000000, light <= 8.100000 -> classify(-) 

    if entry.temperature > 8.00:
        if entry.light <= 8.10:
            entry.label = "-"

###TODO rule pruning###
###TODO rule data defaulting###

  .. image::images/training.png


Problem 3: Naive Bayes Classifier
------------------------------------------------------------


Problem 4: MAP Hypothesis
------------------------------------------------------------


Problem 5: ML Hypothesis
------------------------------------------------------------

