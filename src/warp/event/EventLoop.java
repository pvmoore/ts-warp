package warp.event;

import org.apache.log4j.Logger;
import warp.event.Event.Listener;
import warp.misc.Async;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

final public class EventLoop {
    final private Logger log = Logger.getLogger(EventLoop.class);
    final private ThreadPoolExecutor executors;
    final private Map<Integer, List<Listener>> listeners = new ConcurrentHashMap<>();

    public EventLoop() {
        /*
            Create a pool with one thread per cpu thread.
            Set this number with a property later
        */
        var maxThreads = Runtime.getRuntime().availableProcessors();
        log.debug("Max threads = "+maxThreads);
        this.executors = new ThreadPoolExecutor(maxThreads, maxThreads,
                                                0L, TimeUnit.MILLISECONDS,
                                                new LinkedBlockingQueue<>());

        executors.setThreadFactory(r -> {
            var t = Executors.defaultThreadFactory().newThread(r);
            var name = ""+executors.getPoolSize();
            t.setName("PoolThread-"+name);
            log.debug("Creating new thread "+name);
            return t;
        });
        executors.setRejectedExecutionHandler((Runnable r, ThreadPoolExecutor executor)-> {
            // ignore
        });
        log.debug("EventLoop started");
    }
    @Async
    public EventLoop register(Listener l, int... events) {
        log.trace("registering listener "+l);
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
        log.trace("unregistering listener "+l);
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
        log.trace("firing event: "+event);
        var list = listeners.get(event.key);
        if(list==null) return;

        for(var l : list) {
            executors.submit(()-> {
                //log.trace("  Trigger "+l);
                l.trigger(event);
            });
        }
    }
    @Async
    public void shutdown() {
        shutdown(0);
    }
    @Async
    public void shutdown(long timeoutMillis) {
        log.debug("shutdown called");
        try{
            executors.shutdown();

            if(timeoutMillis!=0) {
                if(!executors.awaitTermination(1, TimeUnit.SECONDS)) {
                    executors.shutdownNow();
                }
            } else {
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
