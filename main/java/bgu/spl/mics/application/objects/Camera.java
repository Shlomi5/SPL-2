package bgu.spl.mics.application.objects;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a camera sensor on the robot.
 * Responsible for detecting objects in the environment.
 */
public class Camera {
    int id;
    int frequency;
    STATUS status;
    LinkedList<StampedDetectedObjects> detectedObjectsList;

    public Camera(int id, int frequency) {
        this.id = id;
        this.frequency = frequency;
        this.status = STATUS.DOWN;
        detectedObjectsList = new LinkedList<>();
    }

    public void checkAndDetectObjects(int time) {

    }

//    public void detectObjects(StampedDetectedObjects detectedObjects) {
//        detectedObjectsList.add(detectedObjects);
//    }



}
