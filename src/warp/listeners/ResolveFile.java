package warp.listeners;

import org.apache.log4j.Logger;
import warp.State;
import warp.event.Event;
import warp.event.EventLoop;
import warp.util.Async;

final public class ResolveFile implements Event.Listener<State> {
    private Logger log = Logger.getLogger(ResolveFile.class);
    private EventLoop events;

    public ResolveFile(EventLoop events) {
        this.events = events;
    }

    @Async
    @Override
    public void trigger(Event<State> event) {
        var state = event.payload;
        log.debug("Resolving "+state.file);


    }
}
