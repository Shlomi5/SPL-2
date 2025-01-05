package main.java.bgu.spl.mics.application.objects;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class LastFrames {
    ConcurrentHashMap<String, StampedDetectedObjects> cameraFrames;
    ConcurrentHashMap<String, List<TrackedObject>> lidarFrames;

    public LastFrames() {
        cameraFrames = new ConcurrentHashMap<>();
        lidarFrames = new ConcurrentHashMap<>();
    }

    public void addCameraFrame(String cameraId, StampedDetectedObjects frame) {
        cameraFrames.put(cameraId, frame);
    }

    public void addLidarFrame(String lidarId, List<TrackedObject> frame) {
        lidarFrames.put(lidarId, frame);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LastFrames{cameraFrames=").append(cameraFrames)
                .append(", lidarFrames=").append(lidarFrames)
                .append('}');
        return sb.toString();
    }

}
