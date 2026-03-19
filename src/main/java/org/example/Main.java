package org.example;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // ---------- configurable parameters ----------
        // GB — total computational quota // can be updated to 10000 for larger inputs, for testing
        int totalQuota = 200;
        List<Integer> workload = Arrays.asList(
            100, 100, 100,
             50,  50,  50,  50,
             20,  20,  20,  20,  20,
             10,  10,  10,  10,  10,  10,
              5,   5,   5,   5,   5,   5,   5,   5
        );
        // ---------------------------------------------

        System.out.println("=== Cluster Configuration Optimizer ===");
        System.out.println();
        System.out.println("Workload: " + workload);
        System.out.printf("Total process data : %d GB across %d given input processes%n",
            workload.stream().mapToInt(Integer::intValue).sum(), workload.size());
        System.out.printf("Total quota        : %,d GB%n", totalQuota);
        System.out.println();

        long startMs = System.currentTimeMillis();
        ClusterConfig optimal = ClusterOptimizer.findOptimal(workload, totalQuota);
        long elapsed = System.currentTimeMillis() - startMs;

        if (optimal == null) {
            System.out.println("No valid configuration found for the given workload.");
            return;
        }

        System.out.println("=== Optimal Configuration ===");
        System.out.printf("  High cores : x = %d, capacity A = %d GB  (quota: %d GB)%n",
            optimal.getNumHighCores(), optimal.getHighCoreCapacity(),
            optimal.getNumHighCores() * optimal.getHighCoreCapacity());
        System.out.printf("  Low cores  : y = %d, capacity B = %d GB  (quota: %d GB)%n",
            optimal.getNumLowCores(), optimal.getLowCoreCapacity(),
            optimal.getNumLowCores() * optimal.getLowCoreCapacity());
        System.out.printf("  Total quota used : %d / %,d GB%n", optimal.totalQuotaUsed(), totalQuota);
        System.out.printf("  Makespan         : %d time units%n", optimal.getMakespan());
        System.out.printf("  (Search completed in %d ms)%n", elapsed);
        System.out.println();

        System.out.println("=== Core Assignment Detail ===");
        for (Core core : optimal.getCores()) {
            if (core.getAssignedProcesses().isEmpty()) continue;
            System.out.printf("  %s Core #%-3d (cap=%3d GB) | load=%4d | processes=%s%n",
                core.getType() == Core.Type.HIGH ? "HIGH" : "LOW ",
                core.getId(),
                core.getCapacity(),
                core.getLoad(),
                core.getAssignedProcesses());
        }

        long idleCores = optimal.getCores().stream()
            .filter(c -> c.getAssignedProcesses().isEmpty()).count();
        if (idleCores > 0) {
            System.out.printf("  (%d cores are idle in this configuration)%n", idleCores);
        }
    }
}
