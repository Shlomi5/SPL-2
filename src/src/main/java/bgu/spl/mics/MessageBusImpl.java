package main.java.bgu.spl.mics;

import main.java.bgu.spl.mics.application.messages.broadcasts.CrashedBroadcast;
import main.java.bgu.spl.mics.application.messages.broadcasts.TerminatedBroadcast;
import main.java.bgu.spl.mics.application.messages.events.PoseEvent;
import main.java.bgu.spl.mics.application.services.FusionSlamService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only one public method (in addition to getters which can be public solely for unit testing) may be added to this class
 * All other methods and members you add the class must be private.
 */
public class MessageBusImpl implements MessageBus {
	final ConcurrentHashMap<MicroService, ConcurrentLinkedQueue<Message>> microServiceQueueHashMap;
	final ConcurrentHashMap<Class<? extends Event<?>>, CopyOnWriteArrayList<MicroService>> eventMicroServicesHashMap;
	final ConcurrentHashMap<Class<? extends Broadcast>, CopyOnWriteArrayList<MicroService>> broadcastMicroServicesHashMap;
	final ConcurrentHashMap<Event<?>, Future<?>> eventFutures;
	final ConcurrentHashMap<Class<? extends Event<?>>, AtomicInteger> roundRobinIndices;

	private static class SingletonHolder {
		private static final MessageBusImpl instance = new MessageBusImpl();
	}
	public static MessageBusImpl getInstance() {
		return SingletonHolder.instance;
	}

	private MessageBusImpl() {
		microServiceQueueHashMap = new ConcurrentHashMap<>();
		eventMicroServicesHashMap = new ConcurrentHashMap<>();
		broadcastMicroServicesHashMap = new ConcurrentHashMap<>();
		eventFutures = new ConcurrentHashMap<>();
		roundRobinIndices = new ConcurrentHashMap<>();
	}



	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		synchronized (eventMicroServicesHashMap) {
			CopyOnWriteArrayList<MicroService> microServices = eventMicroServicesHashMap.get(type);
			if (microServices == null) {
				microServices = new CopyOnWriteArrayList<>();
				eventMicroServicesHashMap.put(type, microServices);
				roundRobinIndices.put(type, new AtomicInteger(0));
			}
			microServices.add(m);
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        CopyOnWriteArrayList<MicroService> microServices = broadcastMicroServicesHashMap.computeIfAbsent(type, k -> new CopyOnWriteArrayList<>());
        microServices.add(m);
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		Future<T> future = (Future<T>) eventFutures.remove(e);
		if (future != null) {
			future.resolve(result);
		}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		CopyOnWriteArrayList<MicroService> microServices = broadcastMicroServicesHashMap.get(b.getClass());
		if (microServices != null) {
			for (MicroService microService : microServices) {
				Queue<Message> messageQueue = microServiceQueueHashMap.get(microService);
				if (messageQueue != null) {
					messageQueue.add(b);
				}
			}
		}

	}

	public <T> Future<T> sendEvent(Event<T> e) {
		CopyOnWriteArrayList<MicroService> subscribers = eventMicroServicesHashMap.get(e.getClass());
		if (subscribers == null || subscribers.isEmpty()) {
			return null;
		}

		Future<T> future = new Future<>();
		eventFutures.put(e, future);

		synchronized (roundRobinIndices.get(e.getClass())) {
			AtomicInteger index = roundRobinIndices.get(e.getClass());
			int nextIndex = index.getAndUpdate(i -> (i + 1) % subscribers.size());

			MicroService assignedMicroService = subscribers.get(nextIndex);
			ConcurrentLinkedQueue<Message> queue = microServiceQueueHashMap.get(assignedMicroService);
			if (queue != null) {
                queue.offer(e);
            }
		}
		return future;
	}

	@Override
	public void register(MicroService m) {
        ConcurrentLinkedQueue<Message> messageQueue =
				microServiceQueueHashMap.computeIfAbsent(m, k -> new ConcurrentLinkedQueue<>());
		System.out.println("Registered " + m.getName());
	}

	@Override
	public void unregister(MicroService m) {
		microServiceQueueHashMap.remove(m);
		synchronized (eventMicroServicesHashMap) {
			eventMicroServicesHashMap.forEach((event, microServices) -> microServices.remove(m));
		}
		synchronized (broadcastMicroServicesHashMap) {
			broadcastMicroServicesHashMap.forEach((broadcast, microServices) -> microServices.remove(m));
		}
	}

	@Override
	public synchronized Message awaitMessage(MicroService m) throws InterruptedException {
		ConcurrentLinkedQueue<Message> messageQueue = microServiceQueueHashMap.get(m);
		if (messageQueue == null) {
			throw new IllegalStateException("MicroService was never registered");
		}

		if (m instanceof FusionSlamService) {
			//printAllMessageQueues();
		}

		long startTime = System.currentTimeMillis();
		long timeout = 500; // Timeout in milliseconds, adjust as needed
		while (messageQueue.isEmpty()) {
			long elapsedTime = System.currentTimeMillis() - startTime;
			long remainingTime = timeout - elapsedTime;

			if (remainingTime <= 0) {
				return null; // Timeout reached, return null or throw an exception
			}

			wait(remainingTime); // Wait for the remaining time
		}

		// Check for CrashedBroadcast or TerminatedBroadcast messages
		Message selectedMessage = null;
		for (Message msg : messageQueue) {
			if (msg instanceof CrashedBroadcast || msg instanceof TerminatedBroadcast) {
				selectedMessage = msg;
				break; // Stop at the first match
			}
		}

		if (selectedMessage != null) {
			messageQueue.remove(selectedMessage); // Remove the selected message
			return selectedMessage; // Return the selected message
		}

		return messageQueue.poll(); // Return the next message if no special message found
	}

	public void printAllMessageQueues() {
		System.out.println("Printing all message queues:");
		for (Map.Entry<MicroService, ConcurrentLinkedQueue<Message>> entry : microServiceQueueHashMap.entrySet()) {
			System.out.println(entry.getKey().getName() + ": " + entry.getValue());
		}
		System.out.println();
	}


	// functions ONLY for tests
	public ConcurrentHashMap<Class<? extends Event<?>>, CopyOnWriteArrayList<MicroService>> getEventMicroServicesHashMap() {
		return eventMicroServicesHashMap;
	}

	public Map<Class<? extends Broadcast>, CopyOnWriteArrayList<MicroService>> getBroadcastMicroServicesHashMap() {
		return broadcastMicroServicesHashMap;
	}


}
