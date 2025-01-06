package main.java.bgu.spl.mics.application.objects;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Error {
    private final String faultySensor;
    private final String error;
    private final AtomicInteger timeStamp;
    private final LastFrames lastFrames;
    private List<Pose> poses;

    public Error(String faultySensor,String error, AtomicInteger timeStamp) {
        this.faultySensor = faultySensor;
        this.error = error;
        this.timeStamp = timeStamp;
        lastFrames = new LastFrames();
    }

    public int getTimeStamp() {
        return timeStamp.get();
    }

    public void addCameraFrame(String cameraId, StampedDetectedObjects frame) {
        lastFrames.addCameraFrame(cameraId, frame);
    }

    public void addLidarFrame(String lidarId, List<TrackedObject> frame) {
        lastFrames.addLidarFrame(lidarId, frame);
    }

    public void setPoses(List<Pose> poses){
        this.poses = poses;
    }



    @Override
    public String toString() {
        return "Error{faultySensor='" + faultySensor + '\'' +
                ", error='" + error + '\'' +
                ", lastFrames=" + lastFrames +
                ", poses=" + poses +
                '}';
    }



}
