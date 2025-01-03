package main.java.bgu.spl.mics.application;

import main.java.bgu.spl.mics.application.objects.Camera;
import main.java.bgu.spl.mics.application.objects.LiDarDataBase;
import main.java.bgu.spl.mics.application.objects.LiDarWorkerTracker;
import main.java.bgu.spl.mics.application.objects.StampedCloudPoints;
import main.java.bgu.spl.mics.application.services.CameraService;
import main.java.bgu.spl.mics.application.services.LiDarService;
import main.java.bgu.spl.mics.application.services.TimeService;

import java.io.FileNotFoundException;
import java.util.List;
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
        Camera camera = new Camera(new AtomicInteger(1), new AtomicInteger(3));
        camera.loadCameraData("example input/camera_data.json", 1);
        CameraService cameraService = new CameraService(camera);
        TimeService timeService = new TimeService(1, 100);


        LiDarWorkerTracker liDarWorkerTracker = new LiDarWorkerTracker(new AtomicInteger(1), new AtomicInteger(3));
        liDarWorkerTracker.LoadDataBase("example input/lidar_data.json");

        LiDarService liDarService = new LiDarService(liDarWorkerTracker);

        Thread liDarThread = new Thread(liDarService);
        Thread cameraThread = new Thread(cameraService);
        Thread timeThread = new Thread(timeService);

        timeThread.start();
        cameraThread.start();
        liDarThread.start();

        // TODO: Parse configuration file.
        // TODO: Initialize system components and services.
        // TODO: Start the simulation.
    }
}
