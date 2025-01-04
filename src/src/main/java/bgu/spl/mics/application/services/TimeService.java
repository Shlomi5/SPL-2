package main.java.bgu.spl.mics.application.services;

import main.java.bgu.spl.mics.MicroService;
import main.java.bgu.spl.mics.application.messages.broadcasts.CrashedBroadcast;
import main.java.bgu.spl.mics.application.messages.broadcasts.TerminatedBroadcast;
import main.java.bgu.spl.mics.application.messages.broadcasts.TickBroadcast;

/**
 * TimeService acts as the global timer for the system, broadcasting TickBroadcast messages
 * at regular intervals and controlling the simulation's duration.
 */
public class TimeService extends MicroService {

    int tickTime;
    int duration;
    int counter = 1;

    /**
     * Constructor for TimeService.
     *
     * @param TickTime The duration of each tick in milliseconds.
     * @param Duration The total number of ticks before the service terminates.
     */
    public TimeService(int TickTime, int Duration) {
        super("TimeService");
        this.tickTime = TickTime;
        this.duration = Duration + 1;
    }

    /**
     * Initializes the TimeService.
     * Starts broadcasting TickBroadcast messages and terminates after the specified duration.
     */
    @Override
    protected void initialize() {

        subscribeBroadcast(CrashedBroadcast.class, (CrashedBroadcast c) -> {
            System.out.println("TimeService Crashed");
            terminate();
        });

        subscribeBroadcast(TickBroadcast.class, (TickBroadcast t) -> {
            System.out.println("TimeService got TickBroadcast");
            try {
                Thread.sleep(tickTime * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("TimeService: " + counter);
            sendBroadcast(new TickBroadcast(counter));
            counter = counter + 1;
        });
        if (counter < duration) {
            sendBroadcast(new TickBroadcast(counter));
        }
        else {
            terminate();
        }
    }
}
