package main.java.bgu.spl.mics.application.messages.events;

import main.java.bgu.spl.mics.Event;
import main.java.bgu.spl.mics.application.objects.DetectedObject;
import main.java.bgu.spl.mics.application.objects.StampedDetectedObjects;

import java.util.List;

public class DetectedObjectsEvent implements Event<StampedDetectedObjects> {
    private final StampedDetectedObjects stampedDetectedObjects;

    public DetectedObjectsEvent(StampedDetectedObjects stampedDetectedObjects) {
        this.stampedDetectedObjects = stampedDetectedObjects;
    }

    public StampedDetectedObjects getStampedDetectedObjects() {
        return stampedDetectedObjects;
    }


}
