package main.java.bgu.spl.mics.application.objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import main.java.bgu.spl.mics.application.services.*;

public class ConfigParser {

    

    public static List<Runnable> ParseConfigFile(String folder,String configFilePath) {
        folder = folder + "/";
        try (FileReader reader = new FileReader(folder + configFilePath)) {
            JsonObject config = JsonParser.parseReader(reader).getAsJsonObject();
            List<Runnable> services = new ArrayList<>();

            System.out.println("Parsing Cameras...");
            List<Camera> cameras = parseCameras(folder,config);
            for (Camera camera : cameras) {
                CameraService cameraService = new CameraService(camera);
                services.add(cameraService);
            }

            System.out.println("Parsing LiDar Workers...");
            List<LiDarWorkerTracker> lidarWorkers = parseLidarWorkers(folder,config);
            for (LiDarWorkerTracker lidarWorker : lidarWorkers) {
                LiDarService liDarService = new LiDarService(lidarWorker);
                services.add(liDarService);
            }
            System.out.println("Parsing TimeService...");
            TimeService timeService = parseTimeService(config);
            services.add(timeService);

            System.out.println("Parsing FusionSlam...");
            FusionSlam fusionSlam = FusionSlam.getInstance();
            FusionSlamService fusionSlamService = new FusionSlamService(fusionSlam);
            services.add(fusionSlamService);

            System.out.println("Parsing GPSIMU...");
            GPSIMUDatabase gpsimuDatabase = GPSIMUDatabase.getInstance(folder + config.get("poseJsonFile").getAsString());
            GPSIMU gpsimu = new GPSIMU(gpsimuDatabase);
            PoseService poseService = new PoseService(gpsimu);
            services.add(poseService);

            return services;


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Camera> parseCameras(String folder ,JsonObject config) {
        List<Camera> cameras = new ArrayList<>();

        JsonObject camerasSection = config.getAsJsonObject("Cameras");
        JsonArray camerasConfigurations = camerasSection.getAsJsonArray("CamerasConfigurations");
        String cameraDataPath = camerasSection.get("camera_datas_path").getAsString();

        for (JsonElement element : camerasConfigurations) {
            Camera camera = getCamera(folder, element, cameraDataPath);
            cameras.add(camera);
        }

        return cameras;
    }

    private static Camera getCamera(String folder, JsonElement element, String cameraDataPath) {
        JsonObject cameraConfig = element.getAsJsonObject();
        AtomicInteger id = new AtomicInteger(cameraConfig.get("id").getAsInt());
        AtomicInteger frequency = new AtomicInteger(cameraConfig.get("frequency").getAsInt());

        // Check FREQ = 0
        if (frequency.get() == 0) {
          frequency.set(1);
        }
        String cameraKey = cameraConfig.get("camera_key").getAsString();

        Camera camera = new Camera(id, frequency, cameraKey);
        camera.loadDataBase(folder + cameraDataPath);
        return camera;
    }

    public static List<LiDarWorkerTracker> parseLidarWorkers(String folder ,JsonObject config) {
        List<LiDarWorkerTracker> lidarWorkers = new ArrayList<>();

        JsonObject lidarSection = config.getAsJsonObject("LiDarWorkers");
        JsonArray lidarConfigurations = lidarSection.getAsJsonArray("LidarConfigurations");
        String lidarDataPath = lidarSection.get("lidars_data_path").getAsString();

        for (JsonElement element : lidarConfigurations) {
            JsonObject lidarConfig = element.getAsJsonObject();
            AtomicInteger id = new AtomicInteger(lidarConfig.get("id").getAsInt());
            AtomicInteger frequency = new AtomicInteger(lidarConfig.get("frequency").getAsInt());

            // Check FREQ = 0
            if (frequency.get() == 0) {
                frequency.set(1);
            }

            LiDarWorkerTracker lidarWorker = new LiDarWorkerTracker(id, frequency);
            lidarWorker.loadDataBase(folder + lidarDataPath);
            lidarWorkers.add(lidarWorker);
        }

        return lidarWorkers;
    }

    public static TimeService parseTimeService(JsonObject config) {
        int tickTime = config.get("TickTime").getAsInt();
        int duration = config.get("Duration").getAsInt();

        AtomicInteger atomicTickTime = new AtomicInteger(tickTime);
        AtomicInteger atomicDuration = new AtomicInteger(duration);

        return new TimeService(atomicTickTime, atomicDuration);
    }


}
