package warp.listeners;

import org.apache.log4j.Logger;
import warp.State;
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
        var state = event.payload;
        log.debug("Parsing "+state.file+" :: "+state.tokens.length()+" tokens");


        // Stop here
        events.shutdown();
    }
}
