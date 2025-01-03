package main.java.bgu.spl.mics.application.objects;

import java.util.List;

/**
 * Manages the fusion of sensor data for simultaneous localization and mapping (SLAM).
 * Combines data from multiple sensors (e.g., LiDAR, camera) to build and update a global map.
 * Implements the Singleton pattern to ensure a single instance of FusionSlam exists.
 */
public class FusionSlam {
    // Singleton instance holder

    //landmarks: Array/List of Landmark – Represents the map of the environment.
    //o Poses: List of type Pose – Represents previous Poses needed for calculations.
    List<LandMark> landmarks;
    List<Pose> poses;

    public static FusionSlam getInstance() {
        return FusionSlamHolder.instance;
    }

    public void addLandmarksFromTrackedObjects(List<TrackedObject> trackedObjects) {
        for (TrackedObject trackedObject : trackedObjects) {
            String id = trackedObject.getId();
            boolean found = false;
            for (LandMark landmark : landmarks) {
                if (landmark.getId().equals(id)) {
                    landmark.getCoordinates().addAll(trackedObject.getCloudPoints());
                    found = true;
                    break;
                }
            }
            if (!found) {
                LandMark newLandmark = new LandMark(id, trackedObject.getDescription(), trackedObject.getCloudPoints());
                landmarks.add(newLandmark);
            }
        }
    }

    public void addPose(Pose newPose) {
        poses.add(newPose);
    }

    public void crash() {
        // TODO Implement this
    }

    public void terminate() {
        // TODO Implement this
    }
    
    private static class FusionSlamHolder {
        private static final FusionSlam instance = new FusionSlam();
    }
}
