package main.java.bgu.spl.mics.application.objects;

import java.util.List;

public class Error {
    private String faultySensor;
    private String error;
    private LastFrames lastFrames;
    private List<Pose> poses;
    private StatisticalFolder statisticalFolder;

    public Error(String faultySensor,String error){
        this.faultySensor = faultySensor;
        this.error = error;
        lastFrames = new LastFrames();
        statisticalFolder = new StatisticalFolder();
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

    public void setStatisticalFolder(StatisticalFolder statisticalFolder){
        this.statisticalFolder = statisticalFolder;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Error{faultySensor='").append(faultySensor).append('\'')
                .append(", error='").append(error).append('\'')
                .append(", lastFrames=").append(lastFrames)
                .append(", poses=").append(poses)
                .append(", statisticalFolder=").append(statisticalFolder)
                .append('}');
        return sb.toString();
    }



}
