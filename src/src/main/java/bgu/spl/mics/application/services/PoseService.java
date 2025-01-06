package main.java.bgu.spl.mics.application.services;

import main.java.bgu.spl.mics.MessageBusImpl;
import main.java.bgu.spl.mics.MicroService;
import main.java.bgu.spl.mics.application.messages.broadcasts.CrashedBroadcast;
import main.java.bgu.spl.mics.application.messages.broadcasts.TerminatedBroadcast;
import main.java.bgu.spl.mics.application.messages.broadcasts.TickBroadcast;
import main.java.bgu.spl.mics.application.messages.events.FinishedData;
import main.java.bgu.spl.mics.application.messages.events.PoseEvent;
import main.java.bgu.spl.mics.application.objects.GPSIMU;


/**
 * PoseService is responsible for maintaining the robot's current pose (position and orientation)
 * and broadcasting PoseEvents at every tick.
 */
public class PoseService extends MicroService {

    final GPSIMU gpsimu;
    /**
     * Constructor for PoseService.
     *
     * @param gpsimu The GPSIMU object that provides the robot's pose data.
     */
    public PoseService(GPSIMU gpsimu) {
        super("PoseService");
        this.gpsimu = gpsimu;
    }

    /**
     * Initializes the PoseService.
     * Subscribes to TickBroadcast and sends PoseEvents at every tick based on the current pose.
     */
    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick) -> {
            synchronized (gpsimu) {
                if (tick.getTime() >= gpsimu.getMaxTick().get()) {
                    sendEvent(new FinishedData(this.getName()));
                    gpsimu.terminate();
                    terminate();
                }
                else {
                    gpsimu.addCurrentPose(tick.getTime());
                    PoseEvent poseEvent = new PoseEvent(gpsimu.getCurrentPose());
                    sendEvent(poseEvent);
                    printMe();
                }
            }
        });
        subscribeBroadcast(CrashedBroadcast.class, (CrashedBroadcast crash) -> {
            gpsimu.crash();
        });
        subscribeBroadcast(TerminatedBroadcast.class, (TerminatedBroadcast terminate) -> {
            gpsimu.terminate();
            terminate();
        });
    }

    private void printMe() {
        try {
            System.out.println("Time: " + gpsimu.getCurrentPose().getTime());
            System.out.println("Position: " + gpsimu.getCurrentPose());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
