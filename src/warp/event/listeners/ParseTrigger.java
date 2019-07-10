package warp.event.listeners;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.actions.ParseAction;
import warp.event.Event;
import warp.event.EventLoop;
import warp.event.WarpEventFactory;
import warp.misc.Async;

final public class ParseTrigger implements Event.Listener<ModuleState> {
    private Logger log = Logger.getLogger(ParseTrigger.class);
    private EventLoop events;
    private WarpEventFactory eventFactory;

    public ParseTrigger(EventLoop events, WarpEventFactory eventFactory) {
        this.events = events;
        this.eventFactory = eventFactory;
    }

    @Async
    @Override
    public void trigger(Event<ModuleState> event) {
        try {
            var state = event.payload;

            /* Parse tokens */
            new ParseAction().run(state);

            state.module.writeToLog("");

            if(state.hasErrors()) {
                /* This module is complete with errors */
                for(var e : state.errors) {
                    log.error("Error: "+e);
                }
            } else {

                /* Trigger lex for all imported modules */
                // todo


                /* Trigger resolution */
                events.fire(eventFactory.resolveFile(event.payload));
            }

        }catch(Throwable t) {
            log.error("Caught error", t);
            events.fire(eventFactory.error(t));
        }

        // Stop here
        events.shutdown();
    }
}
