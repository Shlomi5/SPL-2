package main.java.bgu.spl.mics.application.objects;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CameraDatabase {
    ConcurrentHashMap<String,List<StampedDetectedObjects>> camerasData;

    public List<StampedDetectedObjects> getCameraData(String cameraKey) {
        return camerasData.get(cameraKey);
    }


    private static class SingletonHolder {
        private static final CameraDatabase instance = new CameraDatabase();
    }

    private CameraDatabase() {
        camerasData = new ConcurrentHashMap<>();
    }

    public static CameraDatabase getInstance(String filePath) {
        if (SingletonHolder.instance.camerasData.isEmpty()) {
            SingletonHolder.instance.camerasData = SingletonHolder.instance.parseJsonToConcurrentHashMap(filePath);
        }
        return SingletonHolder.instance;
    }

    private ConcurrentHashMap<String, List<StampedDetectedObjects>> parseJsonToConcurrentHashMap(String jsonFilePath) {
        ConcurrentHashMap<String, List<StampedDetectedObjects>> resultMap = new ConcurrentHashMap<>();

        try (FileReader reader = new FileReader(jsonFilePath)) {
            // Parse JSON file into a JsonObject
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            // Iterate over the cameras (camera1, camera2, etc.)
            for (String cameraKey : jsonObject.keySet()) {
                JsonArray cameraDataArray = jsonObject.getAsJsonArray(cameraKey);
                List<StampedDetectedObjects> stampedList = new ArrayList<>();

                // Process each entry in the camera's data array
                for (JsonElement element : cameraDataArray) {
                    JsonObject timeEntry = element.getAsJsonObject();
                    int time = timeEntry.get("time").getAsInt();

                    List<DetectedObject> detectedObjects = new ArrayList<>();
                    JsonArray detectedArray = timeEntry.getAsJsonArray("detectedObjects");
                    for (JsonElement objElement : detectedArray) {
                        JsonObject detectedObjectJson = objElement.getAsJsonObject();
                        String id = detectedObjectJson.get("id").getAsString();
                        String description = detectedObjectJson.get("description").getAsString();
                        detectedObjects.add(new DetectedObject(id, description));
                    }

                    // Add StampedDetectedObjects to the list
                    stampedList.add(new StampedDetectedObjects(new AtomicInteger(time), detectedObjects));
                }

                // Add the list to the ConcurrentHashMap
                resultMap.put(cameraKey, stampedList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultMap;
    }

}
