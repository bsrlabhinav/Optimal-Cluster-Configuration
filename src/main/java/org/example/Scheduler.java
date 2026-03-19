package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Scheduler {

    /**
     * Assigns processes to cores using the Longest Processing Time (LPT) heuristic.
     *
     * Rules:
     *  - Processes are sorted descending by size so large jobs are placed first.
     *  - A process with size > lowCoreCapacity MUST go to a high core.
     *  - All other processes go to whichever eligible core (high or low) currently
     *    has the lowest load, breaking ties by preferring low cores to preserve
     *    high-core capacity for large jobs.
     *
     * Returns the makespan (load of the most-loaded core), or Integer.MAX_VALUE if
     * any process cannot be accommodated by any core in the cluster.
     */
    public static int schedule(List<Core> cores, List<Integer> processes, int lowCoreCapacity) {
        List<Integer> sorted = new ArrayList<>(processes);
        sorted.sort(Comparator.reverseOrder());

        List<Core> highCores = new ArrayList<>();
        List<Core> lowCores  = new ArrayList<>();
        for (Core c : cores) {
            if (c.getType() == Core.Type.HIGH) highCores.add(c);
            else                               lowCores.add(c);
        }

        for (int size : sorted) {
            if (size > lowCoreCapacity) {
                // Must go to a high core
                Core target = leastLoaded(highCores, size);
                if (target == null) return Integer.MAX_VALUE;
                target.assign(size);
            } else {
                // Prefer the globally least-loaded eligible core; prefer low core on tie
                Core bestLow  = leastLoaded(lowCores,  size);
                Core bestHigh = leastLoaded(highCores, size);

                Core target;
                if (bestLow == null && bestHigh == null) return Integer.MAX_VALUE;
                else if (bestLow  == null) target = bestHigh;
                else if (bestHigh == null) target = bestLow;
                else {
                    // Prefer low core unless high core has strictly less load
                    target = (bestHigh.getLoad() < bestLow.getLoad()) ? bestHigh : bestLow;
                }
                target.assign(size);
            }
        }

        return cores.stream().mapToInt(Core::getLoad).max().orElse(0);
    }

    private static Core leastLoaded(List<Core> candidates, int requiredCapacity) {
        Core best = null;
        for (Core c : candidates) {
            if (c.canAccept(requiredCapacity)) {
                if (best == null || c.getLoad() < best.getLoad()) best = c;
            }
        }
        return best;
    }
}
