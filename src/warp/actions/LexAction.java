package warp.actions;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.event.Event;
import warp.event.EventLoop;
import warp.event.WarpEventFactory;
import warp.lex.Lexer;
import warp.misc.Async;

final public class LexAction implements Event.Listener<ModuleState> {
    final private static Logger log = Logger.getLogger(LexAction.class);
    final private EventLoop events;
    final WarpEventFactory eventFactory;

    public LexAction(EventLoop events, WarpEventFactory eventFactory) {
        this.events = events;
        this.eventFactory = eventFactory;
    }

    @Async
    @Override
    public void trigger(Event<ModuleState> event) {
        var state = event.payload;
        try{
            state.getBarrier().acquire();

            new Lexer(state).lex();

            events.fire(eventFactory.parseFile(state));

        }catch(Exception e) {
            events.fire(eventFactory.error(e));
        }finally{
            state.getBarrier().release();
        }
    }
}
