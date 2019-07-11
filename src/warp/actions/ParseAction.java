package warp.actions;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.event.Event;
import warp.event.EventLoop;
import warp.event.WarpEventFactory;
import warp.misc.Async;
import warp.parse.Parser;

final public class ParseAction implements Event.Listener<ModuleState> {
    final private static Logger log = Logger.getLogger(ParseAction.class);
    final private EventLoop events;
    final private WarpEventFactory eventFactory;

    public ParseAction(EventLoop events, WarpEventFactory eventFactory) {
        this.events = events;
        this.eventFactory = eventFactory;
    }

    @Async
    @Override
    public void trigger(Event<ModuleState> event) {
        var state = event.payload;
        try{
            state.getBarrier().acquire();

            new Parser(state).parse();

            state.module.writeToDebug(state.file.getName()+":");

            if(state.hasErrors()) {
                /* This module is complete with errors */
                log.error("Errors found in file "+state.file);

                for(var e : state.errors) {
                    log.error("\t"+e.line+": "+e.msg);
                }
            } else {

                // todo
                /* Trigger lex for all imported modules */

                // todo
                /* Trigger lex for all <reference path="..." /> */

                /* Trigger resolution */
                events.fire(eventFactory.resolveFile(event.payload));
            }

        }catch(Throwable e) {
            events.fire(eventFactory.error(e));
        }finally{
            state.getBarrier().release();
        }
    }
}
