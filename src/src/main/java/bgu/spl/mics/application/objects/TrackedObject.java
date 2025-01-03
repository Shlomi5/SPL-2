package main.java.bgu.spl.mics.application.objects;

import java.util.List;

/**
 * Represents an object tracked by the LiDAR.
 * This object includes information about the tracked object's ID, description, 
 * time of tracking, and coordinates in the environment.
 */
public class TrackedObject {
    private final String id;
    private final int time;
    private final String description;
    List<CloudPoint> cloudPoints;

    public TrackedObject(String id, int time, String description, List<CloudPoint> cloudPoints) {
        this.id = id;
        this.time = time;
        this.description = description;
        this.cloudPoints = cloudPoints;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TrackedObject{id='").append(id).append('\'')
                .append(", time=").append(time)
                .append(", description='").append(description).append('\'')
                .append(", cloudPoints=").append(cloudPoints)
                .append('}');
        return sb.toString();
    }


}
