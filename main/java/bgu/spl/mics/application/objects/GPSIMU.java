package bgu.spl.mics.application.objects;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the robot's GPS and IMU system.
 * Provides information about the robot's position and movement.
 */
public class GPSIMU {
    private int currentTick;
    STATUS status;
    List<Pose> poseList;

    public GPSIMU(String path) {
        this.poseList = createPoseList(path);
        this.currentTick = 0;
        this.status = STATUS.UP;
    }

    public class PoseCreator {
        private List<Pose> createPoseList(String path) {
            List<Pose> poseList = new ArrayList<>();
            try (FileReader reader = new FileReader(path)) {
                // Create a Gson instance
                Gson gson = new Gson();

                // Define the type of the list
                Type poseListType = new TypeToken<List<PoseJson>>() {}.getType();

                // Parse the JSON file into a list of intermediate objects
                List<PoseJson> jsonPoseList = gson.fromJson(reader, poseListType);

                // Convert each JSON object to a Pose instance
                for (PoseJson poseJson : jsonPoseList) {
                    poseList.add(new Pose(poseJson.getX(), poseJson.getY(), poseJson.getYaw(), poseJson.getTime()));
                }
            } catch (IOException e) {
                e.printStackTrace(); // Handle file read errors
            }
            return poseList;
        }


}


