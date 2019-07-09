package warp.event.listeners;

import org.apache.log4j.Logger;
import warp.State;
import warp.actions.LexAction;
import warp.event.Event;
import warp.event.EventLoop;
import warp.event.WarpEventFactory;
import warp.util.Async;

final public class LexFile implements Event.Listener<State> {
    private static Logger log = Logger.getLogger(LexFile.class);
    private EventLoop events;
    private WarpEventFactory eventFactory;

    public LexFile(EventLoop events, WarpEventFactory eventFactory) {
        this.events = events;
        this.eventFactory = eventFactory;
    }

    @Async
    @Override
    public void trigger(Event<State> event) {
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
