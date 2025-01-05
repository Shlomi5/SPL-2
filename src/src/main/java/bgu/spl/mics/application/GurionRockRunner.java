package main.java.bgu.spl.mics.application;


import main.java.bgu.spl.mics.application.objects.*;
import main.java.bgu.spl.mics.application.services.CameraService;
import main.java.bgu.spl.mics.application.services.FusionSlamService;
import main.java.bgu.spl.mics.application.services.LiDarService;
import main.java.bgu.spl.mics.application.services.TimeService;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
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


        List<Runnable> services = ConfigParser.ParseConfigFile("example input" ,"configuration_file.json");
        for (Runnable service : services) {
            Thread thread = new Thread(service);
            thread.start();
        }
//        Camera camera = new Camera(new AtomicInteger(1), new AtomicInteger(3),"camera1");
//
//        CameraService cameraService = new CameraService(camera);
//
//        TimeService timeService = new TimeService(new AtomicInteger(1), new AtomicInteger(10));
//
//        FusionSlam fusionSlam = new FusionSlam();
//        FusionSlamService fusionSlamService = new FusionSlamService(fusionSlam);
//
//        LiDarWorkerTracker liDarWorkerTracker = new LiDarWorkerTracker(new AtomicInteger(1), new AtomicInteger(5));
//        liDarWorkerTracker.loadDataBase("example input/lidar_data.json");
//        LiDarWorkerTracker liDarWorkerTracker2 = new LiDarWorkerTracker(new AtomicInteger(2), new AtomicInteger(1));
//        liDarWorkerTracker2.loadDataBase("example input/lidar_data.json");
//
//        LiDarService liDarService = new LiDarService(liDarWorkerTracker);
//        LiDarService liDarService2 = new LiDarService(liDarWorkerTracker2);
//
//        Thread liDarThread = new Thread(liDarService);
//        Thread liDarThread2 = new Thread(liDarService2);
//        Thread cameraThread = new Thread(cameraService);
//        Thread timeThread = new Thread(timeService);
//        Thread fusionSlamThread = new Thread(fusionSlamService);
//
//        timeThread.start();
//        cameraThread.start();
//        liDarThread.start();
//        liDarThread2.start();
//        //fusionSlamThread.start();

        // TODO: Parse configuration file.
        // TODO: Initialize system components and services.
        // TODO: Start the simulation.
    }
}
