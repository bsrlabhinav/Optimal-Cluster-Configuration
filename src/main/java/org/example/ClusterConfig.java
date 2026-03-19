package org.example;

import java.util.List;

public class ClusterConfig {

    private final int highCoreCapacity;   // A
    private final int lowCoreCapacity;    // B
    private final int numHighCores;       // x
    private final int numLowCores;        // y
    private final int makespan;
    private final List<Core> cores;

    public ClusterConfig(int highCoreCapacity, int lowCoreCapacity,
                         int numHighCores, int numLowCores,
                         int makespan, List<Core> cores) {
        this.highCoreCapacity = highCoreCapacity;
        this.lowCoreCapacity  = lowCoreCapacity;
        this.numHighCores     = numHighCores;
        this.numLowCores      = numLowCores;
        this.makespan         = makespan;
        this.cores            = cores;
    }

    public int getHighCoreCapacity() { return highCoreCapacity; }
    public int getLowCoreCapacity()  { return lowCoreCapacity; }
    public int getNumHighCores()     { return numHighCores; }
    public int getNumLowCores()      { return numLowCores; }
    public int getMakespan()         { return makespan; }
    public List<Core> getCores()     { return cores; }

    public int totalQuotaUsed() {
        return numHighCores * highCoreCapacity + numLowCores * lowCoreCapacity;
    }

    @Override
    public String toString() {
        return String.format(
            "High cores: x=%d, A=%d GB | Low cores: y=%d, B=%d GB | Quota used: %d/10000 | Makespan: %d",
            numHighCores, highCoreCapacity, numLowCores, lowCoreCapacity,
            totalQuotaUsed(), makespan
        );
    }
}
