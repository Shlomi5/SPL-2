package main.java.bgu.spl.mics.application;


import main.java.bgu.spl.mics.MicroService;
import main.java.bgu.spl.mics.application.objects.*;

import java.io.FileNotFoundException;
import java.util.List;

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

        // Parse configuration file.
        String path = args[0];
        String folder = path.substring(0, path.lastIndexOf("/"));
        String file = path.substring(path.lastIndexOf("/") + 1);

        List<MicroService> services = ConfigParser.ParseConfigFile(folder ,file);
        assert services != null;
        for (MicroService service : services) {
            Thread thread = new Thread(service);
            thread.start();
        }

    }
}
