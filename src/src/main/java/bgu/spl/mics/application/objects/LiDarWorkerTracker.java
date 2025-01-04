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
    private STATUS status;
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

    public void crash() {
        status = STATUS.ERROR;
    }

    public void terminate() {
        status = STATUS.DOWN;
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
            TrackedObject trackedObject = dataBase.getTrackedObject(detectedObject, timeStamp);
            trackedObjects.add(trackedObject);
            StatisticalFolder.getInstance().incrementNumTrackedObjects(1);
        }

        return trackedObjects;


    }

    public void setLastTrackedObjects(List<TrackedObject> allTrackedObjects) {
        this.lastTrackedObjects = allTrackedObjects;
    }
}
