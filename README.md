Cluster Configuration Optimizer

Constraint: x·A + y·B ≤ quota (total quota, default 10,000 GB)

Using idea of 
Search space reduction:
- Candidate values for A and B are drawn from distinct process sizes, since setting a capacity higher than needed wastes quota.
- A = maxvalue among all given process size.
- maxvalue for X = quota / A (scenario for B=0)
- Values for B can be various process sizes less than max Value of a process - A
- B=0 is also included as a "no low cores" option (only high cores used).
- For a fixed (A, B, x), y is maximised as floor((quota - x*A) / B) because more low cores can never increase makespan.


Assigns processes to cores using the Longest Processing Time (LPT).

Rules:
- Processes are sorted descending by size so large jobs are placed first.
- A process with size > lowCoreCapacity MUST go to a high core.
- All other processes go to whichever eligible core (high or low) currently
  has the lowest load, breaking ties by preferring low cores to preserve
  high-core capacity for large jobs.

// ============================= Example 1: Sample Output 10000GB quota

When we have a very high quota 10_000GB processing capacity,
considering capacity as GB processing power instead of units  

=== Cluster Configuration Optimizer ===
Workload: [100, 100, 100, 50, 50, 50, 50, 20, 20, 20, 20, 20, 10, 10, 10, 10, 10, 10, 5, 5, 5, 5, 5, 5, 5, 5]
Total process data : 700 GB across 26 processes  
Total quota        : 10,000 GB  
  
=== Optimal Configuration ===  
High cores : x = 7, capacity A = 100 GB  (quota: 700 GB)  
Low cores  : y = 0, capacity B = 0 GB    (quota: 0 GB)  
Total quota used : 700 / 10,000 GB  
Makespan         : 100 time units  
(Search completed in 25 ms)  

=== Core Assignment Detail ===  
HIGH Core #1   (cap=100 GB) | load= 100 | processes=[100]  
HIGH Core #2   (cap=100 GB) | load= 100 | processes=[100]  
HIGH Core #3   (cap=100 GB) | load= 100 | processes=[100]  
HIGH Core #4   (cap=100 GB) | load= 100 | processes=[50, 20, 20, 5, 5]  
HIGH Core #5   (cap=100 GB) | load= 100 | processes=[50, 20, 10, 10, 5, 5]  
HIGH Core #6   (cap=100 GB) | load= 100 | processes=[50, 20, 10, 10, 5, 5]  
HIGH Core #7   (cap=100 GB) | load= 100 | processes=[50, 20, 10, 10, 5, 5]  


// ============================= Example 2: Sample Output 200GB Quota  

When we have a finite quota 200GB processing capacity  
[considering capacity as GB processing power instead of units]  
=== Cluster Configuration Optimizer ===  

Workload: [100, 100, 100, 50, 50, 50, 50, 20, 20, 20, 20, 20, 10, 10, 10, 10, 10, 10, 5, 5, 5, 5, 5, 5, 5, 5]  
Total process data : 700 GB across 26 given input processes  
Total quota        : 200 GB  

=== Optimal Configuration ===  
High cores : x = 1, capacity A = 100 GB  (quota: 100 GB)  
Low cores  : y = 2, capacity B = 50 GB  (quota: 100 GB)  
Total quota used : 200 / 200 GB  
Makespan         : 300 time units  
(Search completed in 5 ms)  

=== Core Assignment Detail ===  
HIGH Core #1   (cap=100 GB) | load= 300 | processes=[100, 100, 100]  
LOW  Core #2   (cap= 50 GB) | load= 200 | processes=[50, 50, 20, 20, 20, 10, 10, 5, 5, 5, 5]  
LOW  Core #3   (cap= 50 GB) | load= 200 | processes=[50, 50, 20, 20, 10, 10, 10, 10, 5, 5, 5, 5]  
