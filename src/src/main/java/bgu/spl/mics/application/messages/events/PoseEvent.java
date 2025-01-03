package main.java.bgu.spl.mics.application.messages.events;

import main.java.bgu.spl.mics.Event;
import main.java.bgu.spl.mics.EventAbs;
import main.java.bgu.spl.mics.application.objects.Pose;

public class PoseEvent extends EventAbs<Pose> {
    private final Pose pose;

    public PoseEvent(Pose pose) {
        this.pose = pose;
    }

    public Pose getPose() {
        return pose;
    }

    @Override
    public void setFuture() {

    }
}
