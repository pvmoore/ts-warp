package warp.actions;

import org.apache.log4j.Logger;
import warp.ModuleState;

final public class EmitAction {
    private static Logger log = Logger.getLogger(EmitAction.class);

    public void run(ModuleState state) throws Exception {
        log.debug("Emitting "+state.file);


    }
}
