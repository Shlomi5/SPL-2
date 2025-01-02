<<<<<<<< Updated upstream:src/src/main/java/bgu/spl/mics/application/objects/DetectedObject.java
package main.java.bgu.spl.mics.application.objects;
========
package bgu.spl.mics.application.objects;
>>>>>>>> Stashed changes:src/main/java/bgu/spl/mics/application/objects/DetectedObject.java

/**
 * DetectedObject represents an object detected by the camera.
 * It contains information such as the object's ID and description.
 */
public class DetectedObject {

    private final String id;
    private final String description;

    public DetectedObject(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
<<<<<<<< Updated upstream:src/src/main/java/bgu/spl/mics/application/objects/DetectedObject.java
========

    @Override
    public String toString() {
        return "DetectedObject{id='" + id + "', description='" + description + "'}";
    }
>>>>>>>> Stashed changes:src/main/java/bgu/spl/mics/application/objects/DetectedObject.java
}
