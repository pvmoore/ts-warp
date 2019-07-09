package warp.actions;

import org.apache.log4j.Logger;
import warp.State;

final public class ResolveAction {
    private static Logger log = Logger.getLogger(ResolveAction.class);

    public void run(State state) throws Exception {
        log.debug("Resolving "+state.file);


    }
}
