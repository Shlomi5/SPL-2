package main.java.bgu.spl.mics.application.messages.broadcasts;

import main.java.bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast
{
    private final int time;

    public TickBroadcast(int time)
    {
        this.time = time;
    }

    public int getTime()
    {
        return time;
    }

}