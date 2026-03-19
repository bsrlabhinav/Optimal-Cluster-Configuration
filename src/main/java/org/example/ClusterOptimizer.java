package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class ClusterOptimizer {

    /**
     * Finds all meaningful (A, B, x, y) configurations and returns the one
     * with the lowest overall time taken (makespan) for the given workload.
     *
     * Input Param : workload list of process sizes in GB
     * Input Param : quota    total computational quota in GB (sum of all core capacities must not exceed this)
     *
     * Search space reduction:
     *  - Candidate values for A and B are drawn from distinct process sizes,
     *    since setting a capacity higher than needed wastes quota.
     *  - A = maxvalue among all given process size.
     *  - maxvalue for X = quota / A (scenario for B=0)
     *  - Values for B can be various process sizes less than max Value of a process - A
     *  - B=0 is also included as a "no low cores" option (only high cores used).
     *  - For a fixed (A, B, x), y is maximised as floor((quota - x*A) / B)
     *    because more low cores can never increase makespan.
     */
    public static ClusterConfig findOptimal(List<Integer> workload, int quota) {
        TreeSet<Integer> sizes = new TreeSet<>(workload);
        int maxSize = sizes.last();

        ClusterConfig best = null;

        // Candidate capacities: every distinct process size (A must cover the largest)
        List<Integer> candidates = new ArrayList<>(sizes);

        for (int A : candidates) {
            if (A < maxSize) continue; // high core must be able to handle the biggest process

            int maxX = quota / A;

            // B=0 means we only use high cores
            List<Integer> bCandidates = new ArrayList<>();
            bCandidates.add(0);
            for (int b : candidates) {
                if (b < A) bCandidates.add(b);
            }

            for (int B : bCandidates) {
                for (int x = 1; x <= maxX; x++) {
                    int remaining = quota - x * A;
                    int y = (B > 0) ? remaining / B : 0;

                    ClusterConfig cfg = evaluate(workload, A, B, x, y);
                    if (cfg == null) continue;

                    if (best == null || cfg.getMakespan() < best.getMakespan()) {
                        best = cfg;
                    }
                }
            }
        }

        return best;
    }

    private static ClusterConfig evaluate(List<Integer> workload, int A, int B, int x, int y) {
        List<Core> cores = new ArrayList<>();
        int id = 1;
        for (int i = 0; i < x; i++) cores.add(new Core(id++, Core.Type.HIGH, A));
        for (int i = 0; i < y; i++) cores.add(new Core(id++, Core.Type.LOW,  B));

        int effectiveB = (y > 0) ? B : 0;
        int makespan = Scheduler.schedule(cores, workload, effectiveB);
        if (makespan == Integer.MAX_VALUE) return null;

        return new ClusterConfig(A, B, x, y, makespan, cores);
    }
}
