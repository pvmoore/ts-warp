package warp.actions;

import org.apache.log4j.Logger;
import warp.event.Event;
import warp.event.EventLoop;
import warp.misc.Async;

final public class LogAction implements Event.Listener<Event> {
    final private static Logger log = Logger.getLogger(LogAction.class);
    final private EventLoop events;

    public LogAction(EventLoop events) {
        this.events = events;
    }

    @Async
    @Override
    public void trigger(Event<Event> event) {
        log.info("["+event.key+"]");
    }
}
