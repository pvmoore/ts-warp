package warp.actions;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.ModuleFile;
import warp.lex.Tokens;
import warp.parse.ParseStatement;

final public class ParseAction {
    private static Logger log = Logger.getLogger(ParseAction.class);
    private ModuleState state;
    private Tokens tokens;
    private ASTNode node;

    public void run(ModuleState state) throws Exception {
        log.debug("Parsing "+state.file+" :: "+state.tokens.length()+" tokens");
        log.trace(state.tokens.toMultilineString());

        state.module = new ModuleFile(state.file.getName());

        ParseStatement.parse(state, state.module);
    }
}
