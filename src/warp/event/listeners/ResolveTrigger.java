package warp.event.listeners;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.event.Event;
import warp.event.EventLoop;
import warp.misc.Async;

final public class ResolveTrigger implements Event.Listener<ModuleState> {
    private Logger log = Logger.getLogger(ResolveTrigger.class);
    private EventLoop events;

    public ResolveTrigger(EventLoop events) {
        this.events = events;
    }

    @Async
    @Override
    public void trigger(Event<ModuleState> event) {
        /*
            - Check that all required imported files have been parsed. If not then exit
            - Run resolve
            - Fire EMIT_FILE event
         */
        var state = event.payload;
        log.debug("Resolving "+state.file);


    }
}
