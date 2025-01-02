package main.java.bgu.spl.mics.application.objects;

/**
 * Represents the robot's pose (position and orientation) in the environment.
 * Includes x, y coordinates and the yaw angle relative to a global coordinate system.
 */
public class Pose {
    private final double x;
    private final double y;
    private final double yaw;
    private final int Time;

    public Pose(double x, double y, double yaw, int Time) {
        this.x = x;
        this.y = y;
        this.yaw = yaw;
        this.Time = Time;
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
        return Time;
    }

}