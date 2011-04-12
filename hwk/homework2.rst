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
