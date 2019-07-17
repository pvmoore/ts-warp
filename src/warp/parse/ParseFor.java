package warp.parse;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.stmt.ForInStmt;
import warp.ast.stmt.ForOfStmt;
import warp.ast.stmt.ForStmt;
import warp.ast.stmt.Statement;

final public class ParseFor {
    final private static Logger log = Logger.getLogger(ParseFor.class);

    public static Statement parse(ModuleState state, ASTNode parent) {
        log.trace("parseBinary "+state.tokens.get());
        var tokens = state.tokens;
        assert(tokens.isKeyword("for"));

        var p = tokens.scopeIndexOf("of", 2);
        if(p!=-1) {
            return new ForOfStmt().parse(state, parent);
        }

        p = tokens.scopeIndexOf("in", 2);
        if(p!=-1) {
            return new ForInStmt().parse(state, parent);
        }

        return new ForStmt().parse(state, parent);
    }
}
