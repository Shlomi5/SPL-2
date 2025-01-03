package main.java.bgu.spl.mics.application.objects;

/**
 * Represents the robot's pose (position and orientation) in the environment.
 * Includes x, y coordinates and the yaw angle relative to a global coordinate system.
 */
public class Pose {
    private final double x;
    private final double y;
    private final double yaw;
    private final int time;

    public Pose(int time, double x, double y, double yaw) {
        this.x = x;
        this.y = y;
        this.yaw = yaw;
        this.time = time;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getYaw() {
        return yaw;
    }

    public int getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Pose{" +
                "x=" + x +
                ", y=" + y +
                ", yaw=" + yaw +
                ", Time=" + time +
                '}';
    }
}
