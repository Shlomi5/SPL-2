package main.java.bgu.spl.mics.application.messages.broadcasts;

import main.java.bgu.spl.mics.Broadcast;

import java.util.concurrent.atomic.AtomicInteger;

public class TickBroadcast implements Broadcast
{
    private final AtomicInteger time;

    public TickBroadcast(AtomicInteger time)
    {
        this.time = time;
    }

    public int getTime()
    {
        return time.get();
    }

}
