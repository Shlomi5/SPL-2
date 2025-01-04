package main.java.bgu.spl.mics.example.messages;

import main.java.bgu.spl.mics.Event;

public class ExampleEvent implements Event<String>{

    private final String senderName;

    public ExampleEvent(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    @Override
    public void setFuture() {

    }
}