<<<<<<<< Updated upstream:src/src/main/java/bgu/spl/mics/application/objects/LandMark.java
package main.java.bgu.spl.mics.application.objects;
========
package bgu.spl.mics.application.objects;
>>>>>>>> Stashed changes:src/main/java/bgu/spl/mics/application/objects/LandMark.java

import java.util.List;

/**
 * Represents a landmark in the environment map.
 * Landmarks are identified and updated by the FusionSlam service.
 */
public class LandMark {
    private final String id;
    private final String description;
    private final List<CloudPoint> coordinates;

    public LandMark(String id, String description, List<CloudPoint> coordinates) {
        this.id = id;
        this.description = description;
        this.coordinates = coordinates;
    }
}
