package main.java.bgu.spl.mics.application.objects;

import main.java.bgu.spl.mics.MicroService;
import main.java.bgu.spl.mics.application.messages.events.FinishedData;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Manages the fusion of sensor data for simultaneous localization and mapping (SLAM).
 * Combines data from multiple sensors (e.g., LiDAR, camera) to build and update a global map.
 * Implements the Singleton pattern to ensure a single instance of FusionSlam exists.
 */
public class FusionSlam {
    // Singleton instance holder

    // landmarks: Array/List of Landmark – Represents the map of the environment.
    // Poses: List of type Pose – Represents previous Poses needed for calculations.
    private final List<LandMark> landmarks = new CopyOnWriteArrayList<>();
    private final List<Pose> poses = new CopyOnWriteArrayList<>();
    private static final HashMap<String, AtomicBoolean> finishedServices = new HashMap<>();

    private static boolean isInitialized = false; // Tracks whether the method was called
    private STATUS status = STATUS.UP;

    private FusionSlam() {}

    public static FusionSlam getInstance() {
        return FusionSlamHolder.instance;
    }

    public static synchronized FusionSlam getInstance(List<String> services) {
        if (isInitialized) {
            throw new IllegalStateException("FusionSlam has already been initialized with services.");
        }
        synchronized (finishedServices) {
            for (String service : services) {
                finishedServices.putIfAbsent(service, new AtomicBoolean(false));
            }
        }
        isInitialized = true; // Mark as initialized
        return FusionSlamHolder.instance;
    }

    public void addLandmarksFromTrackedObjects(List<TrackedObject> trackedObjects) {
        for (TrackedObject trackedObject : trackedObjects) {
            List<CloudPoint> globalPoints = transformToGlobalCoordinates(poses.get(poses.size() - 1), trackedObject);
            String id = trackedObject.getId();
            boolean found = false;

            for (LandMark landmark : landmarks) {
                if (landmark.getId().equals(id)) {
                    found = true;

                    // Safely update the CloudPoints
                    synchronized (landmark) {
                        int minLength = Math.min(landmark.getCloudPoints().size(), globalPoints.size());
                        for (int i = 0; i < minLength; i++) {
                            CloudPoint existingPoint = landmark.getCloudPoints().get(i);
                            CloudPoint newPoint = globalPoints.get(i);

                            existingPoint.setX((existingPoint.getX() + newPoint.getX()) / 2);
                            existingPoint.setY((existingPoint.getY() + newPoint.getY()) / 2);
                        }

                        if (minLength < globalPoints.size()) {
                            for (int i = minLength; i < globalPoints.size(); i++) {
                                landmark.getCloudPoints().add(globalPoints.get(i));
                            }
                        }
                    }

                    break;
                }
            }

            if (!found) {
                LandMark newLandmark = new LandMark(id, trackedObject.getDescription(), globalPoints);
                landmarks.add(newLandmark);
                StatisticalFolder.getInstance().addLandmark(newLandmark);
            }
        }
        System.out.println("Landmarks Till Now: " + landmarks);
    }

    public void addPose(Pose newPose) {
        poses.add(newPose);
    }

    public void crash() {

    }

    public void terminate() {
        status = STATUS.DOWN;
    }

    public AtomicBoolean MicroServiceFinished(String microService) {
        System.out.println("MicroService " + microService + " finished*********************************************************");
        AtomicBoolean allFinished = new AtomicBoolean(true);
        AtomicInteger counter = new AtomicInteger(0);
        synchronized (finishedServices) {
            finishedServices.get(microService).set(true);
            for (String service : finishedServices.keySet()) {
                if (!finishedServices.get(service).get()) {
                    counter = new AtomicInteger(counter.get() + 1);
                }
                if (counter.get() > 1) { // means that more than only the time service is not finished
                    allFinished.set(false);
                }
            }
        }
        return allFinished;
    }

    private static class FusionSlamHolder {
        private static final FusionSlam instance = new FusionSlam();
    }

    public static List<CloudPoint> transformToGlobalCoordinates(Pose pose, TrackedObject trackedObject) {
        List<CloudPoint> globalCloudPoints = new CopyOnWriteArrayList<>();

        // Extract pose parameters
        double xRobot = pose.getX();
        double yRobot = pose.getY();
        double thetaRad = Math.toRadians(pose.getYaw());

        // Cosine and sine of the yaw angle
        double cosTheta = Math.cos(thetaRad);
        double sinTheta = Math.sin(thetaRad);

        // Transform each CloudPoint
        for (CloudPoint localPoint : trackedObject.getCloudPoints()) {
            double xLocal = localPoint.getX();
            double yLocal = localPoint.getY();

            // Apply rotation
            double xRotated = cosTheta * xLocal - sinTheta * yLocal;
            double yRotated = sinTheta * xLocal + cosTheta * yLocal;

            // Apply translation
            double xGlobal = xRotated + xRobot;
            double yGlobal = yRotated + yRobot;

            // Create a new global CloudPoint
            globalCloudPoints.add(new CloudPoint(xGlobal, yGlobal));
        }

        return globalCloudPoints;
    }


}
