package bgu.spl.mics.application;

import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.objects.Camera;
import bgu.spl.mics.application.services.CameraService;
import bgu.spl.mics.application.services.TimeService;

import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The main entry point for the GurionRock Pro Max Ultra Over 9000 simulation.
 * <p>
 * This class initializes the system and starts the simulation by setting up
 * services, objects, and configurations.
 * </p>
 */
public class GurionRockRunner {

    /**
     * The main method of the simulation.
     * This method sets up the necessary components, parses configuration files,
     * initializes services, and starts the simulation.
     *
     * @param args Command-line arguments. The first argument is expected to be the path to the configuration file.
     */
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Hello World!");


        Camera camera = new Camera(new AtomicInteger(1), new AtomicInteger(5));
        camera.loadCameraData("example input/camera_data.json", 1);
        CameraService cameraService = new CameraService(camera);
        TimeService timeService = new TimeService(1000, 100);

        Thread cameraThread = new Thread(cameraService);
        Thread timeThread = new Thread(timeService);

        timeThread.start();
        cameraThread.start();

        // TODO: Parse configuration file.
        // TODO: Initialize system components and services.
        // TODO: Start the simulation.
    }
}
