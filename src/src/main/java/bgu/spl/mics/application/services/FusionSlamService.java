package main.java.bgu.spl.mics.application.services;

import main.java.bgu.spl.mics.MessageBusImpl;
import main.java.bgu.spl.mics.MicroService;
import main.java.bgu.spl.mics.application.messages.broadcasts.CrashedBroadcast;
import main.java.bgu.spl.mics.application.messages.broadcasts.TerminatedBroadcast;
import main.java.bgu.spl.mics.application.messages.events.FinishedData;
import main.java.bgu.spl.mics.application.messages.events.PoseEvent;
import main.java.bgu.spl.mics.application.messages.events.TrackedObjectsEvent;
import main.java.bgu.spl.mics.application.objects.FusionSlam;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * FusionSlamService integrates data from multiple sensors to build and update
 * the robot's global map.
 */
public class FusionSlamService extends MicroService {
    private final FusionSlam fusionSlam;
    private boolean trackedObjectsReceived = false; // Tracks TrackedObjectsEvent reception
    private boolean poseReceived = false;          // Tracks PoseEvent reception
    private TrackedObjectsEvent lastTrackedObjectsEvent = null;
    private final Object lock = new Object();      // Synchronization lock

    /**
     * Constructor for FusionSlamService.
     *
     * @param fusionSlam The FusionSLAM object responsible for managing the global map.
     */
    public FusionSlamService(FusionSlam fusionSlam) {
        super("FusionSlamService");
        this.fusionSlam = fusionSlam;
    }

    /**
     * Initializes the FusionSlamService.
     */
    @Override
    protected void initialize() {
        subscribeEvent(TrackedObjectsEvent.class, (TrackedObjectsEvent trackedObjects) -> {
            synchronized (lock) {
                trackedObjectsReceived = true;
                lastTrackedObjectsEvent = trackedObjects;

                if (poseReceived) {
                    fusionSlam.addLandmarksFromTrackedObjects(trackedObjects.getTrackedObjects());
                    resetEvents();
                }
            }
        });

        subscribeEvent(PoseEvent.class, (PoseEvent pose) -> {
            synchronized (lock) {
                fusionSlam.addPose(pose.getPose());
                poseReceived = true;
                if (trackedObjectsReceived) {
                    if (lastTrackedObjectsEvent != null) {
                        fusionSlam.addLandmarksFromTrackedObjects(lastTrackedObjectsEvent.getTrackedObjects());
                    }
                    resetEvents();
                }
            }
        });

        subscribeBroadcast(CrashedBroadcast.class, (CrashedBroadcast crash) -> {
            fusionSlam.crash();
        });

        subscribeBroadcast(TerminatedBroadcast.class, (TerminatedBroadcast terminate) -> {
            terminate();
            fusionSlam.terminate();
        });
        subscribeEvent(FinishedData.class, (FinishedData finishedData) -> {
            AtomicBoolean allFinished = fusionSlam.MicroServiceFinished(finishedData.getMicroService());
            if (allFinished.get()) {
                sendBroadcast(new TerminatedBroadcast());
            }
        });
    }

    /**
     * Resets the state of received events.
     */
    private void resetEvents() {
        trackedObjectsReceived = false;
        poseReceived = false;
        lastTrackedObjectsEvent = null;
    }
}
