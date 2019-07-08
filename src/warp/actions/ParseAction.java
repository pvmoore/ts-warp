package warp.actions;

import org.apache.log4j.Logger;
import warp.State;

final public class ParseAction {
    private static Logger log = Logger.getLogger(ParseAction.class);

    public void run(State state) throws Exception {
        log.debug("Parsing "+state.file+" :: "+state.tokens.length()+" tokens");


    }
}
