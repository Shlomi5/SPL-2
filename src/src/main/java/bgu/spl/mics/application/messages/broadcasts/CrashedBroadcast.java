package main.java.bgu.spl.mics.application.messages.broadcasts;

import main.java.bgu.spl.mics.Broadcast;

public class CrashedBroadcast implements Broadcast {
    private String faultySensor;
    private String error;

    public CrashedBroadcast(String faultySensor, String error) {
        this.faultySensor = faultySensor;
        this.error = error;
    }

    public String getFaultySensor() {
        return faultySensor;
    }

    public String getError() {
        return error;
    }

}
