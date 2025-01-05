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


        List<Runnable> services = ConfigParser.ParseConfigFile("example_input_with_error" ,"configuration_file.json");
        for (Runnable service : services) {
            Thread thread = new Thread(service);
            thread.start();
        }

        // TODO: Parse configuration file.
        // TODO: Initialize system components and services.
        // TODO: Start the simulation.
    }
}
