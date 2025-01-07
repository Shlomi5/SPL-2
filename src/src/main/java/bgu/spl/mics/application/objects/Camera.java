package main.java.bgu.spl.mics.application.objects;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a camera sensor on the robot.
 * Responsible for detecting objects in the environment.
 */
public class Camera {
    private final AtomicInteger id;
    private final AtomicInteger frequency;
    private final String cameraKey;
    private STATUS status;
    private List<StampedDetectedObjects> cameraData;
    private StampedDetectedObjects lastStampedDetectedObjects;

    private int lastTime;

    private CameraDatabase cameraDatabase;


    public Camera(AtomicInteger id, AtomicInteger frequency, String cameraKey) {
        this.id = id;
        this.frequency = frequency;
        this.status = STATUS.UP;
        this.cameraKey = cameraKey;
        lastStampedDetectedObjects = new StampedDetectedObjects(new AtomicInteger(0), new CopyOnWriteArrayList<>());
    }

    public void loadDataBase(String path) {
        cameraDatabase = CameraDatabase.getInstance(path);
        cameraData = cameraDatabase.getCameraData(cameraKey);
        lastTime = 0;
        for (StampedDetectedObjects stampedDetectedObjects : cameraData) {
            if (stampedDetectedObjects.getTimestamp() > lastTime) {
                lastTime = stampedDetectedObjects.getTimestamp();
            }
        }
    }


    public int getFrequency() {
        return frequency.get();
    }

    public int getId() {
        return id.get();
    }

    public StampedDetectedObjects getLastStampedDetectedObjects() {
        return lastStampedDetectedObjects;
    }

    public String fullName() {
        return "camera" + id;
    }

    public void crash() {
        status = STATUS.ERROR;
    }

    public void terminate() {
        status = STATUS.DOWN;
    }


    public StampedDetectedObjects checkAndDetectObjects(int time) {
        List<DetectedObject> detectedObjects = getDetectedObjects(time);
        if (detectedObjects.isEmpty()) {
            return null;
        } else {
            StampedDetectedObjects stampedDetectedObjects = new StampedDetectedObjects(new AtomicInteger(time), detectedObjects);

            DetectedObject errorDetectedObject = containsError(detectedObjects);
            if (errorDetectedObject != null) {
                crash();
                List<DetectedObject> errorDetectedObjectList = new CopyOnWriteArrayList<>();
                errorDetectedObjectList.add(errorDetectedObject);
                return new StampedDetectedObjects(new AtomicInteger(time), errorDetectedObjectList);
            } else {
                lastStampedDetectedObjects = stampedDetectedObjects;
                StatisticalFolder.getInstance().incrementNumDetectedObjects(detectedObjects.size());
                return stampedDetectedObjects;
            }

        }

    }

    private List<DetectedObject> getDetectedObjects(int time) {
        int frequencyInt = getFrequency();
        int timeOfLastCapture = time - frequencyInt + 1;
        List<DetectedObject> detectedObjects = new CopyOnWriteArrayList<>();
        for (StampedDetectedObjects stampedObj : cameraData) {
            if ((timeOfLastCapture <= stampedObj.getTimestamp()) && (stampedObj.getTimestamp() <= time)) {
                List<DetectedObject> newDetectedObjects = stampedObj.getDetectedObjects();
                detectedObjects.addAll(newDetectedObjects);
            }
        }
        return detectedObjects;
    }

    private DetectedObject containsError(List<DetectedObject> detectedObjects) {
        for (DetectedObject detectedObject : detectedObjects) {
            if (detectedObject.getId().equals("ERROR")) {
                return detectedObject;
            }
        }
        return null;
    }


    public int getLastTime() {
        return lastTime;
    }

    public boolean crashed() {
        return status == STATUS.ERROR;
    }
}