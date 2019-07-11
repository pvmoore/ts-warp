package warp.actions;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.event.Event;
import warp.event.EventLoop;
import warp.event.WarpEventFactory;
import warp.misc.Async;

final public class ResolveAction implements Event.Listener<ModuleState> {
    final private static Logger log = Logger.getLogger(ResolveAction.class);
    final private EventLoop events;
    final WarpEventFactory eventFactory;

    public ResolveAction(EventLoop events, WarpEventFactory eventFactory) {
        this.events = events;
        this.eventFactory = eventFactory;
    }

    @Async
    @Override
    public void trigger(Event<ModuleState> event) {
        var state = event.payload;
        try{
            state.getBarrier().acquire();

            new Resolver(state).resolve();

            events.fire(eventFactory.parseFile(state));

        }catch(Exception e) {
            events.fire(eventFactory.error(e));
        }finally{
            state.getBarrier().release();
        }
    }

    private static class Resolver {
        final private ModuleState state;

        Resolver(ModuleState state) {
            this.state = state;
        }

        void resolve() throws Exception {
            log.debug("Resolving "+state.file);
        }
    }
}
