package main.java.bgu.spl.mics.application.objects;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents a group of cloud points corresponding to a specific timestamp.
 * Used by the LiDAR system to store and process point cloud data for tracked objects.
 */
public class StampedCloudPoints {

        private AtomicBoolean wasRead;
        private final long timestamp;
        private final String id;
        private final List<CloudPoint> cloudPoints;

        public StampedCloudPoints(long timestamp, String id, List<CloudPoint> cloudPoints) {
            this.wasRead = new AtomicBoolean(false);
            this.timestamp = timestamp;
            this.id = id;
            this.cloudPoints = cloudPoints;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public String getId() {
            return id;
        }

        public void markRead() {
            wasRead.set(true);
        }
        public boolean isRead() {
            return wasRead.get();
        }

    @Override
    public String toString() {
        return String.format("StampedCloudPoint{time=%d, id='%s', cloudPoints=%s}", timestamp, id, cloudPoints);
    }

    public List<CloudPoint> getCloudPoints() {
        return cloudPoints;
    }

    // get cloud points seems shtupid, need to add a way to get the cloud points though
}
