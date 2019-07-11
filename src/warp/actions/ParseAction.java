package warp.actions;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.ast.ModuleFile;
import warp.event.Event;
import warp.event.EventLoop;
import warp.event.WarpEventFactory;
import warp.misc.Async;
import warp.parse.ParseStatement;

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

        }catch(Exception e) {
            events.fire(eventFactory.error(e));
        }finally{
            state.getBarrier().release();
        }

        // Stop here
        events.shutdown();
    }

    private static class Parser {
        final ModuleState state;

        Parser(ModuleState state) {
            this.state = state;
        }
        void parse() throws Exception {
            log.debug("Parsing "+state.file+" :: "+state.tokens.length()+" tokens");
            log.trace(state.tokens.toMultilineString());

            state.module = new ModuleFile(state.file.getName());

            ParseStatement.parse(state, state.module);
        }
    }
}
