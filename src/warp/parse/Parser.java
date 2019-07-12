package warp.parse;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.ast.ModuleFile;

final public class Parser {
    final private static Logger log = Logger.getLogger(Parser.class);
    final ModuleState state;

    public Parser(ModuleState state) {
        this.state = state;
    }
    public void parse() throws Exception {
        log.debug("Parsing "+state.file+" :: "+state.tokens.length()+" tokens");
        log.trace(state.tokens.toMultilineString());

        state.module = new ModuleFile(state.file.getName());

        ParseStatement.parseMultiple(state, state.module);
    }
}