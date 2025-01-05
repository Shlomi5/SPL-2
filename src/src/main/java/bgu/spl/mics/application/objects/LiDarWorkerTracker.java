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

    public void loadDataBase(String path) {
        this.dataBase = LiDarDataBase.getInstance(path);
    }

    public int getId() {
        return id.get();
    }

    public int getFrequency() {
        return frequency.get();
    }

    public List<TrackedObject> getLastTrackedObjects() {
        return lastTrackedObjects;
    }

    public void crash() {
        status = STATUS.ERROR;
    }

    public void terminate() {
        status = STATUS.DOWN;
    }

    public String fullName() {
        return "LiDarWorkerTracker" + id;
    }

    public STATUS getStatus() {
        return status;
    }

    public List<TrackedObject> processDetectedObjectsEvent(DetectedObjectsEvent detectedObjectsEvent) {
        List<TrackedObject> trackedObjects = new ArrayList<>();
        StampedDetectedObjects stampedDetectedObjects= detectedObjectsEvent.getStampedDetectedObjects();
        int timeStamp = stampedDetectedObjects.getTimestamp();
        List<DetectedObject> detectedObjects = stampedDetectedObjects.getDetectedObjects();

        List<StampedCloudPoints> cloudPointsInRange = generateCloudPointsInRange(timeStamp);


        /*for (DetectedObject detectedObject : detectedObjects) {
            TrackedObject trackedObject = dataBase.getTrackedObject(detectedObject, timeStamp);
            System.out.println("LiDarWorkerTracker " + id + " detected object: " + trackedObject);
            if (trackedObject != null){
                trackedObjects.add(trackedObject);
                StatisticalFolder.getInstance().incrementNumTrackedObjects(1);
            }
        }*/

        for (StampedCloudPoints stampedCloudPoints : cloudPointsInRange) {
            if (stampedCloudPoints.isRead()) {
                continue;
            }
            if (stampedCloudPoints.getId().equals( "ERROR")){
                crash();
                return null;
            }
            for (DetectedObject detectedObject : detectedObjects) {
                if (stampedCloudPoints.getId().equals(detectedObject.getId())) {
                    TrackedObject trackedObject = new TrackedObject(detectedObject.getId(), timeStamp, detectedObject.getDescription(), stampedCloudPoints.getCloudPoints());
                    trackedObjects.add(trackedObject);
                    StatisticalFolder.getInstance().incrementNumTrackedObjects(1);
                }
            }
        }

        lastTrackedObjects = trackedObjects;
        return trackedObjects;


    }






    private List<StampedCloudPoints> generateCloudPointsInRange(int timeStamp) {
        int startTime = Math.max(0, timeStamp - frequency.get() + 1);
        List<StampedCloudPoints> cloudPointsInRange = new ArrayList<>();
        for (StampedCloudPoints stampedCloudPoints : dataBase.getCloudPoints()) {
            if (stampedCloudPoints.getTimestamp() >= startTime && stampedCloudPoints.getTimestamp() <= timeStamp) {
                cloudPointsInRange.add(stampedCloudPoints);
            }
        }

        return cloudPointsInRange;
    }


    public int getLastTime() {
        return dataBase.getLastTime();
    }
}
