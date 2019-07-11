package warp.actions;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.event.Event;
import warp.event.EventLoop;
import warp.event.WarpEventFactory;
import warp.misc.Async;

final public class EmitAction implements Event.Listener<ModuleState> {
    final private static Logger log = Logger.getLogger(EmitAction.class);
    final private EventLoop events;
    final WarpEventFactory eventFactory;

    public EmitAction(EventLoop events, WarpEventFactory eventFactory) {
        this.events = events;
        this.eventFactory = eventFactory;
    }

    @Async
    @Override
    public void trigger(Event<ModuleState> event) {
        log.debug("Emitting "+event.payload.file);


        events.shutdown();
    }
}
