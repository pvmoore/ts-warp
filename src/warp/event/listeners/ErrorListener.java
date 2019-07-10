package warp.event.listeners;

import org.apache.log4j.Logger;
import warp.event.Event;
import warp.event.EventLoop;
import warp.misc.Async;

final public class ErrorListener implements Event.Listener<Throwable> {
    private static Logger log = Logger.getLogger(ErrorListener.class);
    private EventLoop events;

    public ErrorListener(EventLoop events) {
        this.events = events;
    }

    @Async
    @Override
    public void trigger(Event<Throwable> event) {
        log.error("Error: "+event.payload.getMessage(), event.payload);

        events.shutdown();
    }
}
