package warp.listeners;

import org.apache.log4j.Logger;
import warp.State;
import warp.event.Event;
import warp.event.EventLoop;
import warp.event.WarpEventFactory;
import warp.parse.Lexer;
import warp.util.Async;

final public class LexFile implements Event.Listener<State> {
    private Logger log = Logger.getLogger(LexFile.class);
    private EventLoop events;
    private WarpEventFactory eventFactory;

    public LexFile(EventLoop events, WarpEventFactory eventFactory) {
        this.events = events;
        this.eventFactory = eventFactory;
    }

    @Async
    @Override
    public void trigger(Event<State> event) {
        log.debug("Lexing "+event.payload.file);

        var tokens = Lexer.lex(event.payload.file);
        event.payload.tokens = tokens;

        events.fire(eventFactory.lexCompleted(event.payload));
    }
}
