package main.java.bgu.spl.mics.application.objects;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents the robot's GPS and IMU system.
 * Provides information about the robot's position and movement.
 */
public class GPSIMU {
    private STATUS status;
    private final List<Pose> poseTillNow;
    private final GPSIMUDatabase gpsimuDatabase;

    public GPSIMU(GPSIMUDatabase gpsimuDatabase) {
        this.poseTillNow = new CopyOnWriteArrayList<>();
        this.gpsimuDatabase = gpsimuDatabase;
        this.status = STATUS.UP;
    }

    public void addCurrentPose(int time) {
        Pose curr = gpsimuDatabase.getPoseList().get(time);
        poseTillNow.add(curr);
    }

    public Pose getCurrentPose() {
        return poseTillNow.get(poseTillNow.size()-1);
    }

    public void crash() {
        status = STATUS.ERROR;
    }

    public void terminate() {
        status = STATUS.DOWN;

    }

    public AtomicInteger getMaxTick() {
        return new AtomicInteger(gpsimuDatabase.getPoseList().size());
    }


    public List<Pose> getPoses() {
        return poseTillNow;
    }
}


