package main.java.bgu.spl.mics.application.messages.broadcasts;

import main.java.bgu.spl.mics.Broadcast;
import main.java.bgu.spl.mics.application.objects.Error;

public class CrashedBroadcast implements Broadcast {

    private Error error;

    public CrashedBroadcast(Error error){
        this.error = error;
    }

    public Error getError(){
        return error;
    }



}
