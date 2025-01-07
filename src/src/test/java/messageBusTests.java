package test.java;

import main.java.bgu.spl.mics.MessageBus;
import main.java.bgu.spl.mics.MessageBusImpl;
import main.java.bgu.spl.mics.application.messages.broadcasts.TickBroadcast;
import main.java.bgu.spl.mics.application.messages.events.PoseEvent;
import main.java.bgu.spl.mics.application.objects.GPSIMU;
import main.java.bgu.spl.mics.application.objects.GPSIMUDatabase;
import main.java.bgu.spl.mics.application.objects.Pose;
import main.java.bgu.spl.mics.application.services.PoseService;
import main.java.bgu.spl.mics.application.services.TimeService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class messageBusTests {
    MessageBusImpl messageBus;
    TimeService timeService;
    GPSIMU gpsimu;
    PoseService poseService;
    GPSIMUDatabase gpsimuDatabase;

    public void setUp() {
        messageBus = MessageBusImpl.getInstance();
        timeService = new TimeService(new AtomicInteger(1), new AtomicInteger(5));
        gpsimuDatabase = GPSIMUDatabase.getInstance("src/test/JsonResources/messageBusResources/pose_data_test1.json");
        gpsimu = new GPSIMU(gpsimuDatabase);
        poseService = new PoseService(gpsimu);
    }

    @Test
    public void testSubscribeEvent() {
        setUp();
        messageBus.register(poseService);
        messageBus.register(timeService);
        messageBus.subscribeEvent(PoseEvent.class, poseService);
        Assertions.assertEquals(1, messageBus.getEventMicroServicesHashMap().get(PoseEvent.class).size());
    }

    @Test
    public void testSubscribeBroadcast() {
        setUp();
        messageBus.register(poseService);
        messageBus.register(timeService);
        messageBus.subscribeBroadcast(TickBroadcast.class, timeService);
        Assertions.assertEquals(1, messageBus.getBroadcastMicroServicesHashMap().get(TickBroadcast.class).size());
    }

    @Test
    public void testSendEvent() {
        setUp();

        messageBus.register(poseService);
        messageBus.register(timeService);
        messageBus.subscribeEvent(PoseEvent.class, poseService);
        messageBus.subscribeBroadcast(TickBroadcast.class, timeService);

        Thread poseThread = new Thread(poseService);
        Thread timeThread = new Thread(timeService);
        poseThread.start();
        timeThread.start();

        while (timeService.getCounter().get() < 6) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        List<Pose> poses = gpsimu.getPoses();
        Assertions.assertEquals(5, poses.size());
        for (int i = 0; i < poses.size(); i++) {
            Assertions.assertEquals(gpsimuDatabase.getPoseList().get(i), poses.get(i));
        }
    }
}
