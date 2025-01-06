package main.java.bgu.spl.mics.application.messages.events;

import main.java.bgu.spl.mics.Event;
import main.java.bgu.spl.mics.MicroService;

public class FinishedData implements Event<String> {
    private final String microServiceName;

    public FinishedData(String microServiceName) {
        this.microServiceName = microServiceName;
    }

    public String getMicroService() {
        return microServiceName;
    }
}
