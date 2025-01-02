package main.java.bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a camera sensor on the robot.
 * Responsible for detecting objects in the environment.
 */
public class Camera {
    int id;
    int frequency;
    STATUS status;
    LinkedList<StampedDetectedObjects> detectedObjectsList;
    ConcurrentHashMap<String, List<StampedDetectedObjects>> cameraData = new ConcurrentHashMap<>();

    public Camera(int id, int frequency) {
        this.id = id;
        this.frequency = frequency;
        this.status = STATUS.UP;
    }

    public void loadCameraData(String path,int id) {
        cameraData = new ConcurrentHashMap<>();

    }

    public void checkAndDetectObjects(int time) {

    }

//    public void detectObjects(StampedDetectedObjects detectedObjects) {
//        detectedObjectsList.add(detectedObjects);
//    }



}
