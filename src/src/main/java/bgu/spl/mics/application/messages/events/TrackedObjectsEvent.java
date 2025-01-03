package main.java.bgu.spl.mics.application.messages.events;

import main.java.bgu.spl.mics.Event;
import main.java.bgu.spl.mics.application.objects.TrackedObject;

import java.util.List;

public class TrackedObjectsEvent implements Event<TrackedObject> {
    private final List<TrackedObject> trackedObjects;

    public TrackedObjectsEvent(List<TrackedObject> trackedObjects) {
        this.trackedObjects = trackedObjects;
    }

    public List<TrackedObject> getTrackedObjects() {
        return trackedObjects;
    }
}
