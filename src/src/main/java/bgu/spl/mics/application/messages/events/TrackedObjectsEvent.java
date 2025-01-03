package main.java.bgu.spl.mics.application.messages.events;

import main.java.bgu.spl.mics.EventAbs;
import main.java.bgu.spl.mics.application.objects.TrackedObject;

import java.util.List;

public class TrackedObjectsEvent extends EventAbs<List<TrackedObject>> {
    private final List<TrackedObject> trackedObjects;

    public TrackedObjectsEvent(List<TrackedObject> trackedObjects) {
        this.trackedObjects = trackedObjects;
    }

    public List<TrackedObject> getTrackedObjects() {
        return trackedObjects;
    }
}
