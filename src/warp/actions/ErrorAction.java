package warp.actions;

import org.apache.log4j.Logger;
import warp.event.Event;
import warp.event.EventLoop;
import warp.misc.Async;

final public class ErrorAction implements Event.Listener<Throwable> {
    final private static Logger log = Logger.getLogger(ErrorAction.class);
    final private EventLoop events;

    public ErrorAction(EventLoop events) {
        this.events = events;
    }

    @Async
    @Override
    public void trigger(Event<Throwable> event) {
        log.error("Error: "+event.payload.getMessage(), event.payload);

        events.shutdown();
    }
}
