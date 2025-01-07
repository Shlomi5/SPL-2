package main.java.bgu.spl.mics.application.services;

import main.java.bgu.spl.mics.MicroService;
import main.java.bgu.spl.mics.application.messages.broadcasts.CrashedBroadcast;
import main.java.bgu.spl.mics.application.messages.broadcasts.TerminatedBroadcast;
import main.java.bgu.spl.mics.application.messages.broadcasts.TickBroadcast;
import main.java.bgu.spl.mics.application.messages.events.DetectedObjectsEvent;
import main.java.bgu.spl.mics.application.messages.events.FinishedData;
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

    public CameraService(Camera camera) {
        super("CameraService " + camera.getId());
        this.camera = camera;
        this.OVER_TIME = calcFinishTime(camera.getLastTime(), camera.getFrequency());
    }

    // calc is slang for calculator
    private int calcFinishTime(int lastTime, int frequency) {
        if (lastTime % frequency == 0) {
            return lastTime;
        } else {
            return lastTime + (frequency - (lastTime % frequency));
        }
    }

    protected void initialize() {
        System.out.println("Got BroadcastTick");
        this.subscribeBroadcast(TickBroadcast.class, (tick) -> {

            if (tick.getTime() > OVER_TIME) {
                System.out.println(camera.fullName() + " is over");
                terminate();
                sendEvent(new FinishedData(getName()));
                return;
            }

            if (tick.getTime() % this.camera.getFrequency() == 0) {
                StampedDetectedObjects detectedObjects = this.camera.checkAndDetectObjects(tick.getTime());

                if (!(detectedObjects ==null)){
                    if (camera.crashed()){
                        System.out.println(camera.fullName() + " Caused an error at time" + detectedObjects.getTimestamp());
                        DetectedObject errorObject = detectedObjects.getDetectedObjects().get(0);
                        Error error = new Error(camera.fullName(), errorObject.getDescription(), new AtomicInteger(detectedObjects.getTimestamp()));
                        sendBroadcast(new CrashedBroadcast(error));
                    }
                    else {
                        DetectedObjectsEvent detectedObjectsEvent = new DetectedObjectsEvent(detectedObjects);
                        sendEvent(detectedObjectsEvent);
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


}

