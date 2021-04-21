# WeightedTardinessProblemGenerator
Problem instance generator for <a href="https://www.cicirello.org/datasets/wtsds/">Weighted Tardiness Scheduling with Sequence-Dependent Setups</a>.
You can find more information on this scheduling problem, including relevant publications, as well as a set of benchmark instances that were generated with this problem instance generator on my website: https://www.cicirello.org/datasets/wtsds/

## Benchmark Problem Instances
You can also download the set of benchmark instances that were generated with this generator in the Harvard Dataverse: http://dx.doi.org/10.7910/DVN/VHA0VQ. As well as here on GitHub: https://github.com/cicirello/scheduling-benchmarks.

## Archived and Not Maintained (effective 4/21/2021)

This repository has been archived and is no longer maintained. If you find it useful, 
feel free to continue to use it. However, we will no longer respond to Issues or 
Pull Requests. This problem instance generator has been integrated into
[Chips-n-Salsa](https://chips-n-salsa.cicirello.org/), a library of
local search algorithms. 
The [source code of Chips-n-Salsa](https://github.com/cicirello/Chips-n-Salsa)
is maintained here on GitHub. You will specifically be interested in the 
`org.cicirello.search.problems.scheduling.WeightedStaticSchedulingWithSetups`
class which is the problem instance generator, and 
the `org.cicirello.search.problems.scheduling.WeightedTardiness` class, which implements
the weighted tardiness cost function.  Search 
the [API documentation](https://chips-n-salsa.cicirello.org/api/) for
more detailed information.
