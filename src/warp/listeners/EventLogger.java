package warp.listeners;

import org.apache.log4j.Logger;
import warp.event.Event;
import warp.event.EventLoop;
import warp.util.Async;

final public class EventLogger implements Event.Listener<Event> {
    private Logger log = Logger.getLogger(EventLogger.class);
    private EventLoop events;

    public EventLogger(EventLoop events) {
        this.events = events;
    }

    @Async
    @Override
    public void trigger(Event<Event> event) {
        log.info("["+event.key+"]");

    }
}
