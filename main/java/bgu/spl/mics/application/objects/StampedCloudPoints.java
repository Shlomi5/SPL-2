package bgu.spl.mics.application.objects;

import java.util.List;

/**
 * Represents a group of cloud points corresponding to a specific timestamp.
 * Used by the LiDAR system to store and process point cloud data for tracked objects.
 */
public class StampedCloudPoints {

        private final long timestamp;
        private final String id;
        private final List<CloudPoint> cloudPoints;

        public StampedCloudPoints(long timestamp, String id, List<CloudPoint> cloudPoints) {
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

        // get cloud points seems shtupid, need to add a way to get the cloud points though
}
