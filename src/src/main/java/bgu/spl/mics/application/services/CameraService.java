package main.java.bgu.spl.mics.application.services;

import main.java.bgu.spl.mics.MicroService;
import main.java.bgu.spl.mics.application.messages.broadcasts.CrashedBroadcast;
import main.java.bgu.spl.mics.application.messages.broadcasts.TerminatedBroadcast;
import main.java.bgu.spl.mics.application.messages.broadcasts.TickBroadcast;
import main.java.bgu.spl.mics.application.messages.events.DetectedObjectsEvent;
import main.java.bgu.spl.mics.application.objects.Camera;
import main.java.bgu.spl.mics.application.objects.DetectedObject;
import main.java.bgu.spl.mics.application.objects.StampedDetectedObjects;

/**
 * CameraService is responsible for processing data from the camera and
 * sending DetectObjectsEvents to LiDAR workers.
 * 
 * This service interacts with the Camera object to detect objects and updates
 * the system's StatisticalFolder upon sending its observations.
 */
public class CameraService extends MicroService {
    private Camera camera;

    public CameraService(Camera camera) {
        super("CameraService " + camera.getId());
        this.camera = camera;
    }

    protected void initialize() {
        System.out.println("Got BroadcastTick");
        this.subscribeBroadcast(TickBroadcast.class, (tick) -> {
            if (tick.getTime() % this.camera.getFrequency() == 0) {
                StampedDetectedObjects detectedObjects = this.camera.checkAndDetectObjects(tick.getTime());
                if (!detectedObjects.getDetectedObjects().isEmpty()) {
                    DetectedObjectsEvent detectedObjectsEvent = new DetectedObjectsEvent(detectedObjects);
                    sendEvent(detectedObjectsEvent);
                    this.printDetectedObjects(detectedObjects);
                }
            }

        });
        this.subscribeBroadcast(TerminatedBroadcast.class, (broadcast) -> {
            System.out.println("CameraService " + camera.getId() + " terminated");
            camera.terminate();
            this.terminate();
        });

        this.subscribeBroadcast(CrashedBroadcast.class, (broadcast) -> {
            System.out.println("CameraService " + camera.getId() + " crashed");
            camera.crash();
            this.terminate();
        });




    }

    private void printDetectedObjects(StampedDetectedObjects detectedObjects) {
        System.out.println("Time: " + detectedObjects.getTimestamp());
        System.out.println("Detected Objects:");

        for (DetectedObject obj : detectedObjects.getDetectedObjects()) {
            System.out.println("  - " + obj);
        }
    }

}

