package main.java.bgu.spl.mics.application.objects;
import main.java.bgu.spl.mics.MessageBusImpl;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Holds statistical information about the system's operation.
 * This class aggregates metrics such as the runtime of the system,
 * the number of objects detected and tracked, and the number of landmarks identified.
 */
public class StatisticalFolder {

    private static class SingletonHolder {
        private static final StatisticalFolder instance = new StatisticalFolder();
    }
    public static StatisticalFolder getInstance() {
        return SingletonHolder.instance;
    }

    // Fields
    private final AtomicInteger systemRuntime;      // Total runtime of the system in ticks
    private final AtomicInteger numDetectedObjects; // Cumulative count of detected objects
    private final AtomicInteger numTrackedObjects;  // Cumulative count of tracked objects
    private final AtomicInteger numLandmarks;       // Total number of unique landmarks

    // Constructor
    public StatisticalFolder() {
        this.systemRuntime = new AtomicInteger(0);
        this.numDetectedObjects = new AtomicInteger(0);
        this.numTrackedObjects = new AtomicInteger(0);
        this.numLandmarks = new AtomicInteger(0);
    }

    // Getters
    public int getSystemRuntime() {
        return systemRuntime.get();
    }

    public int getNumDetectedObjects() {
        return numDetectedObjects.get();
    }

    public int getNumTrackedObjects() {
        return numTrackedObjects.get();
    }

    public int getNumLandmarks() {
        return numLandmarks.get();
    }

    // Update Methods
    public void incrementSystemRuntime(int ticks) {
        systemRuntime.addAndGet(ticks);
    }

    public void incrementNumDetectedObjects(int count) {
        numDetectedObjects.addAndGet(count);
    }

    public void incrementNumTrackedObjects(int count) {
        numTrackedObjects.addAndGet(count);
    }

    public void incrementNumLandmarks(int count) {
        numLandmarks.addAndGet(count);
    }

    @Override
    public String toString() {
        return "StatisticalFolder{" +
                "systemRuntime=" + systemRuntime.get() +
                ", numDetectedObjects=" + numDetectedObjects.get() +
                ", numTrackedObjects=" + numTrackedObjects.get() +
                ", numLandmarks=" + numLandmarks.get() +
                '}';
    }
}

