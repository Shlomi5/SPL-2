package main.java.bgu.spl.mics.application.objects;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GPSIMUDatabase {

    private List<Pose> poseList;
    private String path; // Add a field to store the path

    private List<Pose> createPoseList(String path) {

        CopyOnWriteArrayList<Pose> poseList = new CopyOnWriteArrayList<>();
        try (FileReader reader = new FileReader(path)) {
            // Create a Gson instance
            Gson gson = new Gson();

            // Define the type of the list
            Type poseListType = new TypeToken<List<Pose>>() {}.getType();

            // Parse the JSON file into a list of intermediate objects
            List<Pose> poseListTemp = gson.fromJson(reader, poseListType);

            // Convert each JSON object to a Pose instance
            for (Pose pose : poseListTemp) {
                poseList.add(new Pose(pose.getTime(), pose.getX(), pose.getY(), pose.getYaw()));
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file read errors
        }
        return poseList;
    }

    public List<Pose> getPoseList() {
        return poseList;
    }

    private static class SingletonHolder {
        private static final GPSIMUDatabase instance = new GPSIMUDatabase();
    }

    public static GPSIMUDatabase getInstance(String path) {
        if (SingletonHolder.instance.poseList == null && path != null) {
            // Set the path only once
            SingletonHolder.instance.path = path;
            SingletonHolder.instance.poseList = SingletonHolder.instance.createPoseList(path);
        }
        return SingletonHolder.instance;
    }

    // Optionally, a method to ensure the path is not set multiple times
    public static GPSIMUDatabase getInstance() {
        if (SingletonHolder.instance.poseList == null) {
            throw new IllegalStateException("Path must be provided to initialize the instance.");
        }
        return SingletonHolder.instance;
    }
}
