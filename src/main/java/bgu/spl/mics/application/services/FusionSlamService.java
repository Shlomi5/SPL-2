<<<<<<<< Updated upstream:src/src/main/java/bgu/spl/mics/application/services/FusionSlamService.java
package main.java.bgu.spl.mics.application.services;

import main.java.bgu.spl.mics.MicroService;
========
package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
>>>>>>>> Stashed changes:src/main/java/bgu/spl/mics/application/services/FusionSlamService.java
import bgu.spl.mics.application.objects.FusionSlam;

/**
 * FusionSlamService integrates data from multiple sensors to build and update
 * the robot's global map.
 * 
 * This service receives TrackedObjectsEvents from LiDAR workers and PoseEvents from the PoseService,
 * transforming and updating the map with new landmarks.
 */
public class FusionSlamService extends MicroService {
    /**
     * Constructor for FusionSlamService.
     *
     * @param fusionSlam The FusionSLAM object responsible for managing the global map.
     */
    public FusionSlamService(FusionSlam fusionSlam) {
        super("Change_This_Name");
        // TODO Implement this
    }

    /**
     * Initializes the FusionSlamService.
     * Registers the service to handle TrackedObjectsEvents, PoseEvents, and TickBroadcasts,
     * and sets up callbacks for updating the global map.
     */
    @Override
    protected void initialize() {
        // TODO Implement this
    }
}
