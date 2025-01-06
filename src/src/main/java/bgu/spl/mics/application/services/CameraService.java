package main.java.bgu.spl.mics.application.services;

import main.java.bgu.spl.mics.MicroService;
import main.java.bgu.spl.mics.application.messages.broadcasts.CrashedBroadcast;
import main.java.bgu.spl.mics.application.messages.broadcasts.TerminatedBroadcast;
import main.java.bgu.spl.mics.application.messages.broadcasts.TickBroadcast;
import main.java.bgu.spl.mics.application.messages.events.DetectedObjectsEvent;
import main.java.bgu.spl.mics.application.objects.*;
import main.java.bgu.spl.mics.application.objects.Error;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CameraService is responsible for processing data from the camera and
 * sending DetectObjectsEvents to LiDAR workers.
 * This service interacts with the Camera object to detect objects and updates
 * the system's StatisticalFolder upon sending its observations.
 */
public class CameraService extends MicroService {
    private final Camera camera;
    private final int OVER_TIME;
    private boolean isOver = false;

    public CameraService(Camera camera) {
        super("CameraService " + camera.getId());
        this.camera = camera;
        this.OVER_TIME = camera.getLastTime();
    }

    protected void initialize() {
        System.out.println("Got BroadcastTick");
        this.subscribeBroadcast(TickBroadcast.class, (tick) -> {

            if (tick.getTime() > OVER_TIME && !isOver) {
                System.out.println(camera.fullName() + " is over");
                isOver = true;
                return;
            }

            if (tick.getTime() % this.camera.getFrequency() == 0) {
                StampedDetectedObjects detectedObjects = this.camera.checkAndDetectObjects(tick.getTime());

                if (!(detectedObjects ==null)){
                    if (camera.crashed()){
                        System.out.println(camera.fullName() + " Caused an error");
                        DetectedObject errorObject = detectedObjects.getDetectedObjects().get(0);
                        Error error = new Error(camera.fullName(), errorObject.getDescription(), new AtomicInteger(detectedObjects.getTimestamp()));
                        sendBroadcast(new CrashedBroadcast(error));
                    }
                    else {
                        DetectedObjectsEvent detectedObjectsEvent = new DetectedObjectsEvent(detectedObjects);
                        sendEvent(detectedObjectsEvent);
                        this.printDetectedObjects(detectedObjects);
                    }
                }

            }

        });


        this.subscribeBroadcast(TerminatedBroadcast.class, (broadcast) -> {
            System.out.println(camera.fullName() + " terminated");
            camera.terminate();
            this.terminate();
        });

        this.subscribeBroadcast(CrashedBroadcast.class, (broadcast) -> {
            broadcast.getError().addCameraFrame(camera.fullName(), camera.getLastStampedDetectedObjects());
            camera.crash();
            System.out.println(camera.fullName() + " crashed");
            this.terminate();
        });



    }

    private boolean checkForError(StampedDetectedObjects detectedObjects) {
        for (DetectedObject obj : detectedObjects.getDetectedObjects()) {
            if (obj.getId().equals("ERROR")) {
                Error error = new Error(camera.fullName(), obj.getDescription(), new AtomicInteger(detectedObjects.getTimestamp()));
                sendBroadcast(new CrashedBroadcast(error));
                return true;
            }
        }
        return false;
    }

    private void printDetectedObjects(StampedDetectedObjects detectedObjects) {
        System.out.println("Time: " + detectedObjects.getTimestamp());
        System.out.println( getName() + " Detected Objects:");

        for (DetectedObject obj : detectedObjects.getDetectedObjects()) {
            System.out.println("  - " + obj);
        }
    }


}

