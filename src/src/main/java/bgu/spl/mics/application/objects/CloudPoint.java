<<<<<<<< Updated upstream:src/src/main/java/bgu/spl/mics/application/objects/CloudPoint.java
package main.java.bgu.spl.mics.application.objects;
========
package bgu.spl.mics.application.objects;
>>>>>>>> Stashed changes:src/main/java/bgu/spl/mics/application/objects/CloudPoint.java

/**
 * CloudPoint represents a specific point in a 3D space as detected by the LiDAR.
 * These points are used to generate a point cloud representing objects in the environment.
 */
public class CloudPoint {
    private final double x;
    private final double y;

    public CloudPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
