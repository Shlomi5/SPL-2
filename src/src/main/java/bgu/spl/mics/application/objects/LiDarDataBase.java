package main.java.bgu.spl.mics.application.objects;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import main.java.bgu.spl.mics.MessageBusImpl;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * LiDarDataBase is a singleton class responsible for managing LiDAR data.
 * It provides access to cloud point data and other relevant information for tracked objects.
 */
public class LiDarDataBase {


    private List<StampedCloudPoints> cloudPoints;

    public TrackedObject getTrackedObject(DetectedObject detectedObject, int timeStamp) {
        String detectedObjectId = detectedObject.getId();
        for (StampedCloudPoints stampedCloudPoints : cloudPoints) {
            System.out.println("LidarDataBase: ");
            if (stampedCloudPoints.getId().equals(detectedObjectId) && stampedCloudPoints.getTimestamp() == timeStamp) {
                System.out.println("FUCK YEAHhdshsadkajkadkdwadaawkdwadakw");
                return new TrackedObject(detectedObjectId, timeStamp,detectedObject.getDescription(),stampedCloudPoints.getCloudPoints());
            }
        }
        return null;
    }

    private static class SingletonHolder {
        private static final LiDarDataBase instance = new LiDarDataBase();
    }

    /**
     * Returns the singleton instance of LiDarDataBase.
     *
     * @param filePath The path to the LiDAR data file.
     * @return The singleton instance of LiDarDataBase.
     */
    public static LiDarDataBase getInstance(String filePath) {
        if (SingletonHolder.instance.cloudPoints.isEmpty()) {
            SingletonHolder.instance.cloudPoints = loadCloudPoints(filePath);
        }
        return SingletonHolder.instance;

    }

    private static List<StampedCloudPoints> loadCloudPoints(String filePath) {
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Register custom deserializer for StampedCloudPoints
        gsonBuilder.registerTypeAdapter(StampedCloudPoints.class, new JsonDeserializer<StampedCloudPoints>() {
            @Override
            public StampedCloudPoints deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject jsonObject = json.getAsJsonObject();

                // Extract the basic fields
                int time = jsonObject.get("time").getAsInt();
                String id = jsonObject.get("id").getAsString();

                // Deserialize cloudPoints as List<double[]>
                JsonArray cloudPointsArray = jsonObject.getAsJsonArray("cloudPoints");
                List<CloudPoint> cloudPoints = new ArrayList<>();
                for (JsonElement point : cloudPointsArray) {
                    JsonArray pointArray = point.getAsJsonArray();
                    double x = pointArray.get(0).getAsDouble();
                    double y = pointArray.get(1).getAsDouble();
                    CloudPoint cloudPoint = new CloudPoint(x, y); // Assuming z is fixed to 0.104
                    cloudPoints.add(cloudPoint);
                }

                // Create and return the StampedCloudPoints object
                StampedCloudPoints stampedCloudPoints = new StampedCloudPoints(time, id, cloudPoints);
                return stampedCloudPoints;
            }
        });

        // Create Gson instance with the custom deserializer
        Gson gson = gsonBuilder.create();

        try (FileReader reader = new FileReader(filePath)) {
            Type listType = new TypeToken<List<StampedCloudPoints>>() {}.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Handle error appropriately in production code
        }
    }

    private LiDarDataBase() {
        cloudPoints = new CopyOnWriteArrayList<>();
    }
}
