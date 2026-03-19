package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Core {

    public enum Type { HIGH, LOW }

    private final int id;
    private final Type type;
    private final int capacity;
    private final List<Integer> assignedProcesses = new ArrayList<>();
    private int load = 0;

    public Core(int id, Type type, int capacity) {
        this.id = id;
        this.type = type;
        this.capacity = capacity;
    }

    public boolean canAccept(int processSize) {
        return processSize <= capacity;
    }

    public void assign(int processSize) {
        assignedProcesses.add(processSize);
        load += processSize;
    }

    public int getId() { return id; }
    public Type getType() { return type; }
    public int getCapacity() { return capacity; }
    public int getLoad() { return load; }
    public List<Integer> getAssignedProcesses() { return Collections.unmodifiableList(assignedProcesses); }

    public Core copy() {
        Core c = new Core(id, type, capacity);
        for (int p : assignedProcesses) c.assign(p);
        return c;
    }
}
