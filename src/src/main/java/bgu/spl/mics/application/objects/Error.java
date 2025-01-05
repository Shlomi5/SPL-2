package main.java.bgu.spl.mics.application.objects;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Error {
    private String faultySensor;
    private String error;
    private AtomicInteger timeStamp;
    private LastFrames lastFrames;
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
        StringBuilder sb = new StringBuilder();
        sb.append("Error{faultySensor='").append(faultySensor).append('\'')
                .append(", error='").append(error).append('\'')
                .append(", lastFrames=").append(lastFrames)
                .append(", poses=").append(poses)
                .append('}');
        return sb.toString();
    }



}
