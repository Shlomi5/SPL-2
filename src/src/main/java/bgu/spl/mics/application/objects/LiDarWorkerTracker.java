package main.java.bgu.spl.mics.application.objects;

import main.java.bgu.spl.mics.application.messages.events.DetectedObjectsEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * LiDarWorkerTracker is responsible for managing a LiDAR worker.
 * It processes DetectObjectsEvents and generates TrackedObjectsEvents by using data from the LiDarDataBase.
 * Each worker tracks objects and sends observations to the FusionSlam service.
 */
public class LiDarWorkerTracker {
    private final AtomicInteger id;
    private final AtomicInteger frequency;
    private final STATUS status;
    private List<TrackedObject> lastTrackedObjects;
    private LiDarDataBase dataBase;

    public LiDarWorkerTracker(AtomicInteger id, AtomicInteger frequency) {
        this.id = id;
        this.frequency = frequency;
        this.status = STATUS.UP;
        lastTrackedObjects = new ArrayList<>();
    }

    public void LoadDataBase(String path) {
        this.dataBase = LiDarDataBase.getInstance(path);
    }

    public int getId() {
        return id.get();
    }

    public int getFrequency() {
        return frequency.get();
    }

    public STATUS getStatus() {
        return status;
    }

    public List<TrackedObject> processDetectedObjectsEvent(DetectedObjectsEvent detectedObjectsEvent) {
        List<TrackedObject> trackedObjects = new ArrayList<>();
        StampedDetectedObjects stampedDetectedObjects= detectedObjectsEvent.getStampedDetectedObjects();
        int timeStamp = stampedDetectedObjects.getTimestamp();
        List<DetectedObject> detectedObjects = stampedDetectedObjects.getDetectedObjects();

        for (DetectedObject detectedObject : detectedObjects) {
            System.out.println("LidarWorkerTracker: ");
            System.out.println("Detected object: " + detectedObject);
            TrackedObject trackedObject = dataBase.getTrackedObject(detectedObject, timeStamp);
            trackedObjects.add(trackedObject);
        }

        return trackedObjects;


    }
}
