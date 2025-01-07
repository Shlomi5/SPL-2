package main.java.bgu.spl.mics.application.services;

import main.java.bgu.spl.mics.MicroService;
import main.java.bgu.spl.mics.application.messages.broadcasts.CrashedBroadcast;
import main.java.bgu.spl.mics.application.messages.broadcasts.TerminatedBroadcast;
import main.java.bgu.spl.mics.application.messages.broadcasts.TickBroadcast;
import main.java.bgu.spl.mics.application.objects.StatisticalFolder;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TimeService acts as the global timer for the system, broadcasting TickBroadcast messages
 * at regular intervals and controlling the simulation's duration.
 */
public class TimeService extends MicroService {

    AtomicInteger tickTime;
    AtomicInteger duration;
    AtomicInteger counter = new AtomicInteger(1);

    /**
     * Constructor for TimeService.
     *
     * @param TickTime The duration of each tick in milliseconds.
     * @param Duration The total number of ticks before the service terminates.
     */
    public TimeService(AtomicInteger TickTime, AtomicInteger Duration) {
        super("TimeService");
        this.tickTime = TickTime;
        this.duration = new AtomicInteger(Duration.get());
    }

    /**
     * Initializes the TimeService.
     * Starts broadcasting TickBroadcast messages and terminates after the specified duration.
     */
    @Override
    protected void initialize() {

        subscribeBroadcast(CrashedBroadcast.class, (CrashedBroadcast c) -> {
            System.out.println("TimeService Crashed");
            StatisticalFolder.setTimeStamp(c.getError().getTimeStamp());
            StatisticalFolder.setError(c.getError());
            StatisticalFolder.getInstance().writeJsonToFile("OutputError.json");
            terminate();
        });

        subscribeBroadcast(TerminatedBroadcast.class, (TerminatedBroadcast t) -> {
            System.out.println("TimeService Terminated");
            StatisticalFolder.getInstance().writeJsonToFile("output_file.json");
            terminate();
        });

        subscribeBroadcast(TickBroadcast.class, (TickBroadcast t) -> {
            try {
                Thread.sleep(tickTime.get() * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("TimeService: " + counter);
            StatisticalFolder.getInstance().incrementSystemRuntime(1);
            counter = new AtomicInteger(counter.get() + 1);
            sendBroadcast(new TickBroadcast(counter));

            if (counter.get() > duration.get()) {
                sendBroadcast(new TerminatedBroadcast());
            }
        });
        if (counter.get() < duration.get()) {
            sendBroadcast(new TickBroadcast(counter));
        }
        else {
            sendBroadcast(new TerminatedBroadcast());
            terminate();
        }
    }

    public AtomicInteger getCounter() {
        return counter;
    }
}
