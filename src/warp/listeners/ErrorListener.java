package warp.listeners;

import org.apache.log4j.Logger;
import warp.event.Event;
import warp.event.EventLoop;
import warp.util.Async;

final public class ErrorListener implements Event.Listener<Error> {
    private Logger log = Logger.getLogger(ErrorListener.class);
    private EventLoop events;

    public ErrorListener(EventLoop events) {
        this.events = events;
    }

    @Async
    @Override
    public void trigger(Event<Error> event) {
        log.error("Error: "+event.payload);

        events.shutdown();
    }
}
