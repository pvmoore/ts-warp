package warp.actions;

import org.apache.log4j.Logger;
import warp.ModuleState;

final public class ResolveAction {
    private static Logger log = Logger.getLogger(ResolveAction.class);

    public void run(ModuleState state) throws Exception {
        log.debug("Resolving "+state.file);


    }
}
