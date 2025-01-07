package main.java.bgu.spl.mics.application.objects;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Holds statistical information about the system's operation.
 * This class aggregates metrics such as the runtime of the system,
 * the number of objects detected and tracked, and the number of landmarks identified.
 */
public class StatisticalFolder {

    public static void setTimeStamp(int timeStamp) {
        SingletonHolder.instance.systemRuntime.set(timeStamp);
    }

    public static void setError(Error error) {
        SingletonHolder.instance.error = error;
    }




    public void removeLandmark(String id) {
        for (LandMark landmark : landmarks) {
            if (landmark.getId().equals(id)) {
                landmarks.remove(landmark);
                return;
            }
        }
    }

    private static class SingletonHolder {
        private static final StatisticalFolder instance = new StatisticalFolder();
    }
    public static StatisticalFolder getInstance() {
        return SingletonHolder.instance;
    }

    // Fields
    private Error  error;
    private final AtomicInteger systemRuntime;      // Total runtime of the system in ticks
    private final AtomicInteger numDetectedObjects; // Cumulative count of detected objects
    private final AtomicInteger numTrackedObjects;  // Cumulative count of tracked objects
    private final List<LandMark> landmarks;        // List of landmarks identified in the environment

    // Constructor
    public StatisticalFolder() {
        this.systemRuntime = new AtomicInteger(0);
        this.numDetectedObjects = new AtomicInteger(0);
        this.numTrackedObjects = new AtomicInteger(0);
        this.landmarks = new CopyOnWriteArrayList<>();
    }



    // Update Methods
    public void incrementSystemRuntime(int ticks) {
        systemRuntime.addAndGet(ticks);
    }

    public void incrementNumDetectedObjects(int count) {
        numDetectedObjects.addAndGet(count);
    }

    public void incrementNumTrackedObjects(int count) {
        numTrackedObjects.addAndGet(count);
    }

    public void addLandmark(LandMark landmark) {
        landmarks.add(landmark);
    }

    @Override
    public String toString() {
        return "StatisticalFolder{" +
                "systemRuntime=" + systemRuntime +
                ", numDetectedObjects=" + numDetectedObjects +
                ", numTrackedObjects=" + numTrackedObjects +
                ", landmarks=" + landmarks +
                '}';
    }

    public String createJson() {
        LinkedHashMap<String, Object> jsonMap = new LinkedHashMap<>(); // Use LinkedHashMap to preserve order

        if (error != null) {
            jsonMap.put("Error", error);
        }
        jsonMap.put("systemRuntime", systemRuntime.get());
        jsonMap.put("numDetectedObjects", numDetectedObjects.get());
        jsonMap.put("numTrackedObjects", numTrackedObjects.get());
        jsonMap.put("numLandmarks", landmarks.size());

        HashMap<String, LandMark> landmarkMap = new HashMap<>();
        for (LandMark landmark : landmarks) {
            landmarkMap.put(landmark.getId(), landmark);
        }
        jsonMap.put("landMarks", landmarkMap);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonMap);
    }

    public static void writeJsonToFile(String fileName) {
        String jsonData  = SingletonHolder.instance.createJson();
        // Fixed file name
        Path path = Paths.get(fileName);
        try {
            Files.write(path, jsonData.getBytes()); // Create and write to file
            System.out.println("JSON data written to file: " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing JSON to file: " + e.getMessage());
        }
    }


}

