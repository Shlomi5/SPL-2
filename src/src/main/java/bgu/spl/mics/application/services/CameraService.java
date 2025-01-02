package main.java.bgu.spl.mics.application.services;

import main.java.bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.broadcasts.TerminatedBroadcast;
import bgu.spl.mics.application.messages.broadcasts.TickBroadcast;
import bgu.spl.mics.application.objects.Camera;

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



        subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick) -> {
            camera.checkAndDetectObjects(tick.getTime());
        });
        subscribeBroadcast(TerminatedBroadcast.class,(TerminatedBroadcast broacast) -> {
            // terminate(); ??? maybe 

        } );

    }
}