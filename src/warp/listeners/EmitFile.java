package warp.listeners;

import org.apache.log4j.Logger;
import warp.event.Event;
import warp.event.EventLoop;
import warp.util.Async;

final public class EmitFile implements Event.Listener<Object> {
    private static Logger log = Logger.getLogger(EmitFile.class);
    private EventLoop events;

    public EmitFile(EventLoop events) {
        this.events = events;
    }

    @Async
    @Override
    public void trigger(Event<Object> event) {
        log.debug("Triggered");


        events.shutdown();
    }
}
