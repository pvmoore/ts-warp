package warp.event.listeners;

import org.apache.log4j.Logger;
import warp.event.Event;
import warp.event.EventLoop;
import warp.misc.Async;

final public class EmitTrigger implements Event.Listener<Object> {
    private static Logger log = Logger.getLogger(EmitTrigger.class);
    private EventLoop events;

    public EmitTrigger(EventLoop events) {
        this.events = events;
    }

    @Async
    @Override
    public void trigger(Event<Object> event) {
        log.debug("Triggered");


        events.shutdown();
    }
}
