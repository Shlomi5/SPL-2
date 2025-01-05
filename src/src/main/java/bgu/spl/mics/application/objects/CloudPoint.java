package main.java.bgu.spl.mics.application.objects;

/**
 * CloudPoint represents a specific point in a 3D space as detected by the LiDAR.
 * These points are used to generate a point cloud representing objects in the environment.
 */
public class CloudPoint {
    private double x;
    private double y;

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

    @Override
    public String toString() {
        return String.format("CloudPoint{x=%.2f, y=%.2f}", x, y);
    }

    public void setX(double v) {
        this.x = v;
    }

    public void setY(double v) {
        this.y = v;
    }

}
