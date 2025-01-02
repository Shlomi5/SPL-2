<<<<<<<< Updated upstream:src/src/main/java/bgu/spl/mics/application/services/CameraService.java
package main.java.bgu.spl.mics.application.services;

import main.java.bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.broadcasts.TerminatedBroadcast;
import bgu.spl.mics.application.messages.broadcasts.TickBroadcast;
import bgu.spl.mics.application.objects.Camera;
========
package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.broadcasts.TerminatedBroadcast;
import bgu.spl.mics.application.messages.broadcasts.TickBroadcast;
import bgu.spl.mics.application.messages.events.DetectObjectsEvent;
import bgu.spl.mics.application.objects.Camera;
import bgu.spl.mics.application.objects.DetectedObject;
import bgu.spl.mics.application.objects.StampedDetectedObjects;
>>>>>>>> Stashed changes:src/main/java/bgu/spl/mics/application/services/CameraService.java

/**
 * CameraService is responsible for processing data from the camera and
 * sending DetectObjectsEvents to LiDAR workers.
 * 
 * This service interacts with the Camera object to detect objects and updates
 * the system's StatisticalFolder upon sending its observations.
 */
public class CameraService extends MicroService {

    private Camera camera;

    /**
     * Constructor for CameraService.
     *
     * @param camera The Camera object that this service will use to detect objects.
     */
    public CameraService(Camera camera) {
        super("CameraService");
        this.camera = camera;
    }

    /**
     * Initializes the CameraService.
     * Registers the service to handle TickBroadcasts and sets up callbacks for sending
     * DetectObjectsEvents.
     */
    @Override
    protected void initialize() {


<<<<<<<< Updated upstream:src/src/main/java/bgu/spl/mics/application/services/CameraService.java

        subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick) -> {
            camera.checkAndDetectObjects(tick.getTime());
========
        System.out.println("Got BroadcastTick");
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick) -> {
            if (tick.getTime()%camera.getFrequency() == 0 ){
                StampedDetectedObjects detectedObjects = camera.checkAndDetectObjects(tick.getTime());
                if (detectedObjects!=null){
                    DetectObjectsEvent detectObjectsEvent = new DetectObjectsEvent();
                    printMe(detectedObjects);
                }
            }

>>>>>>>> Stashed changes:src/main/java/bgu/spl/mics/application/services/CameraService.java
        });
        subscribeBroadcast(TerminatedBroadcast.class,(TerminatedBroadcast broacast) -> {
            // terminate(); ??? maybe 

        } );

    }
<<<<<<<< Updated upstream:src/src/main/java/bgu/spl/mics/application/services/CameraService.java
========

    private void printMe(StampedDetectedObjects detectedObjects) {
        System.out.println("Time: " + detectedObjects.getTimestamp());
        System.out.println("Detected Objects:");
        for (DetectedObject obj : detectedObjects.getObjects()) {
            System.out.println("  - " + obj);
        }

    }
>>>>>>>> Stashed changes:src/main/java/bgu/spl/mics/application/services/CameraService.java
}
