package main.java.bgu.spl.mics.application.services;

import main.java.bgu.spl.mics.MicroService;
import main.java.bgu.spl.mics.application.messages.broadcasts.TickBroadcast;
import main.java.bgu.spl.mics.application.messages.events.DetectedObjectsEvent;
import main.java.bgu.spl.mics.application.messages.events.TrackedObjectsEvent;
import main.java.bgu.spl.mics.application.objects.LiDarWorkerTracker;
import main.java.bgu.spl.mics.application.objects.TrackedObject;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * LiDarService is responsible for processing data from the LiDAR sensor and
 * sending TrackedObjectsEvents to the FusionSLAM service.
 * <p>
 * This service interacts with the LiDarWorkerTracker object to retrieve and process
 * cloud point data and updates the system's StatisticalFolder upon sending its
 * observations.
 */
public class LiDarService extends MicroService {


    private final LiDarWorkerTracker LiDarWorkerTracker;
    private ConcurrentLinkedQueue<DetectedObjectsEvent> detectedObjectsEvents;

    /**
     * Constructor for LiDarService.
     *
     * @param LiDarWorkerTracker A LiDAR Tracker worker object that this service will use to process data.
     */
    public LiDarService(LiDarWorkerTracker LiDarWorkerTracker) {
        super("LidarService");
        this.LiDarWorkerTracker = LiDarWorkerTracker;
        detectedObjectsEvents = new ConcurrentLinkedQueue<>();
        // TODO Implement this
    }

    /**
     * Initializes the LiDarService.
     * Registers the service to handle DetectObjectsEvents and TickBroadcasts,
     * and sets up the necessary callbacks for processing data.
     */
    @Override
    protected void initialize() {
        subscribeEvent(DetectedObjectsEvent.class, (event) -> {
            System.out.println("Lidar " + LiDarWorkerTracker.getId() + " received detected objects");
            detectedObjectsEvents.add(event);
        });

        subscribeBroadcast(TickBroadcast.class, (tick) -> {
            if (tick.getTime() % LiDarWorkerTracker.getFrequency() == 0) {
                while (!detectedObjectsEvents.isEmpty()) {
                    System.out.println("Lidar " + LiDarWorkerTracker.getId() + " is processing detected objects");
                    DetectedObjectsEvent detectedObjectsEvent = detectedObjectsEvents.poll();


                    List<TrackedObject> trackedObjects = LiDarWorkerTracker.processDetectedObjectsEvent(detectedObjectsEvent);
                    for (TrackedObject trackedObject : trackedObjects) {
                        System.out.println(trackedObject);
                    }

                    TrackedObjectsEvent trackedObjectsEvent = new TrackedObjectsEvent(trackedObjects);
                    sendEvent(trackedObjectsEvent);

                }
            }
        });


    }


}
