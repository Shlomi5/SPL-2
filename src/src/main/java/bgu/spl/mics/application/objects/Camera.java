package main.java.bgu.spl.mics.application.objects;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a camera sensor on the robot.
 * Responsible for detecting objects in the environment.
 */
public class Camera {
    private AtomicInteger id;
    private AtomicInteger frequency;
    private STATUS status;
    private List<StampedDetectedObjects> cameraData;


    public Camera(AtomicInteger id, AtomicInteger frequency) {
        this.id = id;
        this.frequency = frequency;
        this.status = STATUS.UP;
    }

    public int getFrequency() {
        return frequency.get();
    }

    public void loadCameraData(String path,int id) throws FileNotFoundException {
        // Initialize the Gson parser
        Gson gson = new Gson();

        // Read the JSON file
        JsonObject rootObject = gson.fromJson(new FileReader(path), JsonObject.class);

        // Check if the camera exists
        String cameraKey = "camera" + id;
        if (!rootObject.has(cameraKey)) {
            throw new IllegalArgumentException("Camera with ID " + id + " not found in the JSON file.");
        }

        // Get the camera data
        JsonArray cameraArray = rootObject.getAsJsonArray(cameraKey);


        // Parse the data into StampedDetectedObject
        cameraData = new CopyOnWriteArrayList<>();
        Type detectedObjectListType = new TypeToken<List<DetectedObject>>() {}.getType();

        for (JsonElement element : cameraArray) {
            JsonObject timeFrameObject = element.getAsJsonObject();

            int time = timeFrameObject.get("time").getAsInt();
            List<DetectedObject> detectedObjects = gson.fromJson(timeFrameObject.get("detectedObjects"), detectedObjectListType);

            cameraData.add(new StampedDetectedObjects(new AtomicInteger(time), detectedObjects));
        }

    }

    public StampedDetectedObjects checkAndDetectObjects(int time) {
        int frequencyInt = getFrequency();
        int timeOfLastCapture = time - frequencyInt + 1;
        List<DetectedObject> newObjects = new CopyOnWriteArrayList<>();
        for (StampedDetectedObjects stampedObj : cameraData){
            if ((timeOfLastCapture <= stampedObj.getTimestamp()) && (stampedObj.getTimestamp() <= time)){
                newObjects.addAll(stampedObj.getDetectedObjects());
            }
        }
        if (newObjects!=null){
            StampedDetectedObjects stampedDetectedObjects = new StampedDetectedObjects(new AtomicInteger(time),newObjects);
            return stampedDetectedObjects;
        }
        else{
            return null;
        }
    }



//    public void detectObjects(StampedDetectedObjects detectedObjects) {
//        detectedObjectsList.add(detectedObjects);
//    }



}