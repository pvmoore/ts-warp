package warp.event.listeners;

import org.apache.log4j.Logger;
import warp.event.Event;
import warp.event.EventLoop;
import warp.util.Async;

final public class Log implements Event.Listener<Event> {
    private static Logger log = Logger.getLogger(Log.class);
    private EventLoop events;

    public Log(EventLoop events) {
        this.events = events;
    }

    @Async
    @Override
    public void trigger(Event<Event> event) {
        log.info("["+event.key+"]");

    }
}
