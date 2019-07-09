package warp.event.listeners;

import org.apache.log4j.Logger;
import warp.State;
import warp.actions.ParseAction;
import warp.event.Event;
import warp.event.EventLoop;
import warp.event.WarpEventFactory;
import warp.util.Async;

final public class ParseTokens implements Event.Listener<State> {
    private Logger log = Logger.getLogger(ParseTokens.class);
    private EventLoop events;
    private WarpEventFactory eventFactory;

    public ParseTokens(EventLoop events, WarpEventFactory eventFactory) {
        this.events = events;
        this.eventFactory = eventFactory;
    }

    @Async
    @Override
    public void trigger(Event<State> event) {
        /*
            - Parse tokens
            - Fire LEX_FILE for all referenced imported files
            - Fire RESOLVE_FILE
         */
        try {
            new ParseAction().run(event.payload);

            //events.fire(eventFactory.resolveFile(event.payload));

        }catch(Throwable t) {
            events.fire(eventFactory.error(t));
        }

        // Stop here
        events.shutdown();
    }
}
