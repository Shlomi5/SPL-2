package main.java.bgu.spl.mics.application.objects;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents objects detected by the camera at a specific timestamp.
 * Includes the time of detection and a list of detected objects.
 */
public class StampedDetectedObjects {

        private final AtomicInteger timestamp;
        private final List<DetectedObject> objects;

        public StampedDetectedObjects(AtomicInteger timestamp, List<DetectedObject> objects) {
            this.timestamp = timestamp;
            this.objects = objects;
        }

        public int getTimestamp() {
            return timestamp.get();
        }

        public List<DetectedObject> getDetectedObjects() {
            return objects;
        }
}
