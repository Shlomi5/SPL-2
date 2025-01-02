package main.java.bgu.spl.mics.application.objects;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents the robot's GPS and IMU system.
 * Provides information about the robot's position and movement.
 */
public class GPSIMU {
    private AtomicInteger currentTick;
    private STATUS status;
    private List<Pose> poseList;
    private List<Pose> poseTillNow;

    public GPSIMU(String path) {
        PoseCreator poseCreator = new PoseCreator();
        this.poseTillNow = new CopyOnWriteArrayList<>();
        this.poseList = poseCreator.createPoseList(path);
        this.currentTick.set(0);
        this.status = STATUS.UP;
    }

    public void addCurrentPose(int time) {
        for (Pose pose : poseTillNow) {
            if(pose.getTime() == time) {
                return;
            }
        }
        Pose curr = poseList.get(currentTick.get());
        poseTillNow.add(curr);
        currentTick.incrementAndGet();
    }

    public Pose getCurrentPose() throws Exception {
        if(poseTillNow.isEmpty()) {
            throw new Exception("pose array is empty");
        }
        return poseTillNow.getLast();
    }

    public void crash() {
        //TODO
    }

    public void terminate() {
        //TODO
    }

    public class PoseCreator {
        private List<Pose> createPoseList(String path) {
            try (FileReader reader = new FileReader(path)) {
                // Create a Gson instance
                Gson gson = new Gson();

                // Define the type of the list
                Type poseListType = new TypeToken<List<Pose>>() {
                }.getType();

                // Parse the JSON file into a list of intermediate objects
                CopyOnWriteArrayList<Pose> poseList = gson.fromJson(reader, poseListType);

                // Convert each JSON object to a Pose instance
                for (Pose pose : poseList) {
                    poseList.add(new Pose(pose.getX(), pose.getY(), pose.getYaw(), pose.getTime()));
                }
            } catch (IOException e) {
                e.printStackTrace(); // Handle file read errors
            }
            return poseList;
        }
    }
}


