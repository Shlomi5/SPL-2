package main.java.bgu.spl.mics;

public abstract class EventAbs<T> implements Event<T> {
    Future<T> future;
}
