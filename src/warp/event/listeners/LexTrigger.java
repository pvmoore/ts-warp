package warp.event.listeners;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.actions.LexAction;
import warp.event.Event;
import warp.event.EventLoop;
import warp.event.WarpEventFactory;
import warp.misc.Async;

final public class LexTrigger implements Event.Listener<ModuleState> {
    private static Logger log = Logger.getLogger(LexTrigger.class);
    private EventLoop events;
    private WarpEventFactory eventFactory;

    public LexTrigger(EventLoop events, WarpEventFactory eventFactory) {
        this.events = events;
        this.eventFactory = eventFactory;
    }

    @Async
    @Override
    public void trigger(Event<ModuleState> event) {
        /*
            - Run LexAction
            - Fire PARSE_FILE event
         */
        try {
            new LexAction().run(event.payload);

            events.fire(eventFactory.parseFile(event.payload));

        }catch(Throwable t) {
            events.fire(eventFactory.error(t));
        }
    }
}
