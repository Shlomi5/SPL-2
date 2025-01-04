package main.java.bgu.spl.mics.application.objects;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents an object tracked by the LiDAR.
 * This object includes information about the tracked object's ID, description,
 * time of tracking, and coordinates in the environment.
 */
public final class TrackedObject {
    private final String id;
    private final AtomicInteger time;
    private final String description;
    private final List<CloudPoint> cloudPoints;

    public TrackedObject(String id, int time, String description, List<CloudPoint> cloudPoints) {
        this.id = id;
        this.time = new AtomicInteger(time);
        this.description = description;
        this.cloudPoints = new CopyOnWriteArrayList<>(cloudPoints);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TrackedObject{id='").append(id).append('\'')
                .append(", time=").append(time.get())
                .append(", description='").append(description).append('\'')
                .append(", cloudPoints=").append(cloudPoints)
                .append('}');
        return sb.toString();
    }

    public String getId() {
        return id;
    }

    public int getTime() {
        return time.get();
    }

    public void setTime(int newTime) {
        time.set(newTime); // Thread-safe update
    }

    public int incrementTime() {
        return time.incrementAndGet(); // Thread-safe increment
    }

    public String getDescription() {
        return description;
    }

    public List<CloudPoint> getCloudPoints() {
        return Collections.unmodifiableList(cloudPoints);
    }
}
