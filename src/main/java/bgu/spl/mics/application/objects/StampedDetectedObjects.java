<<<<<<<< Updated upstream:src/src/main/java/bgu/spl/mics/application/objects/StampedDetectedObjects.java
package main.java.bgu.spl.mics.application.objects;
========
package bgu.spl.mics.application.objects;
>>>>>>>> Stashed changes:src/main/java/bgu/spl/mics/application/objects/StampedDetectedObjects.java

import java.util.List;

/**
 * Represents objects detected by the camera at a specific timestamp.
 * Includes the time of detection and a list of detected objects.
 */
public class StampedDetectedObjects {

        private final long timestamp;
        private final List<DetectedObject> objects;

        public StampedDetectedObjects(long timestamp, List<DetectedObject> objects) {
            this.timestamp = timestamp;
            this.objects = objects;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public List<DetectedObject> getObjects() {
            return objects;
        }
}
