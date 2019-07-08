package warp.event;

import org.apache.log4j.Logger;
import warp.event.Event.Listener;
import warp.util.Async;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

final public class EventLoop {
    private Logger log = Logger.getLogger(EventLoop.class);
    private ExecutorService executors;
    private Map<Integer, List<Listener>> listeners = new ConcurrentHashMap<>();

    public EventLoop() {
        /*
            Create a pool with one thread per cpu core.
            Set this number with a property later
        */
        var maxThreads = Runtime.getRuntime().availableProcessors();
        this.executors = Executors.newFixedThreadPool(maxThreads);
        log.debug("EventLoop started");
    }
    @Async
    public EventLoop register(Listener l, int... events) {
        for(var e : events) {
            listeners.compute(e, (key, oldValue) -> {
                if(oldValue == null) {
                    oldValue = new ArrayList<>();
                }
                oldValue.add(l);
                return oldValue;
            });
        }
        return this;
    }
    @Async
    public EventLoop unregister(Listener l, int... events) {
        for(var e : events) {
            listeners.computeIfPresent(e, (key, oldValue) -> {
                oldValue.remove(l);
                return oldValue;
            });
        }
        return this;
    }
    @Async
    public <T> void fire(Event<T> event) {
        //log.trace("Fire: "+event.key);
        var list = listeners.get(event.key);
        if(list==null) return;

        for(var l : list) {
            executors.submit(()-> {
                //log.trace("  Trigger "+l);
                l.trigger(event);
            });
        }
    }
    /**
     *  Called from the main thread.
     */
    public void loop() {
//        try {
//            condition.await();
//        }catch(InterruptedException e) {
//            // ignore
//        }
    }
    @Async
    public void shutdown() {
        log.debug("EventLoop shutting down");
        try{
            executors.shutdown();

            if(!executors.awaitTermination(2, TimeUnit.SECONDS)) {
                executors.shutdownNow();
            }
        }catch(InterruptedException e) {
            // ignore
        }
    }
    @Async
    @Override public String toString() {
        var buf = new StringBuilder("[EventLoop");

        for(var entry : listeners.entrySet()) {
            buf.append("\n  ")
               .append(entry.getKey())
               .append(": ")
               .append(entry.getValue());
        }
        return buf.append("]").toString();
    }
}
