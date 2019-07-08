package warp.listeners;

import org.apache.log4j.Logger;
import warp.event.Event;
import warp.event.EventLoop;
import warp.util.Async;

final public class EmitFiles implements Event.Listener<Object> {
    private Logger log = Logger.getLogger(EmitFiles.class);
    private EventLoop events;

    public EmitFiles(EventLoop events) {
        this.events = events;
    }

    @Async
    @Override
    public void trigger(Event<Object> event) {
        log.debug("Triggered");


        events.shutdown();
    }
}
