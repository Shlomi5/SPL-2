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
    private AtomicInteger currentTick;
    private STATUS status;
    private final List<Pose> poseTillNow;
    private final GPSIMUDatabase gpsimuDatabase;

    public GPSIMU(GPSIMUDatabase gpsimuDatabase) {
        this.poseTillNow = new CopyOnWriteArrayList<>();
        this.gpsimuDatabase = gpsimuDatabase;
        this.currentTick = new AtomicInteger(0);
        this.status = STATUS.UP;
    }

    public void addCurrentPose(int time) {
        for (Pose pose : poseTillNow) {
            if(pose.getTime() == time) {
                return;
            }
        }
        Pose curr = gpsimuDatabase.getPoseList().get(time);
        poseTillNow.add(curr);
        this.currentTick = new AtomicInteger(currentTick.incrementAndGet());
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


    public List<Pose> getPoses() {
        return poseTillNow;
    }
}


