package warp.actions;

import org.apache.log4j.Logger;
import warp.State;
import warp.ast.ASTNode;
import warp.ast.ASTRoot;
import warp.ast.Variable;
import warp.lex.Tokens;
import warp.parse.ParseError;

final public class ParseAction {
    private static Logger log = Logger.getLogger(ParseAction.class);
    private State state;
    private Tokens tokens;
    private ASTNode node;

    public void run(State state) throws Exception {
        log.debug("Parsing "+state.file+" :: "+state.tokens.length()+" tokens");
        log.trace(state.tokens.toMultilineString());

        state.root = new ASTRoot();

        this.node = state.root;
        this.state = state;
        this.tokens = state.tokens;

        var t = tokens.get();

        switch(t.value) {
            case "let":
            case "const":
                parseVariable();
                break;
            default:
                break;
        }

        throw new ParseError("Parse failed in file ["+state.file+"] @ "+tokens.get());
    }
    /**
     *  let | const IDENTIFIER [ : TYPE ] [ = EXPRESSION ]
     */
    private void parseVariable() {
        var v = new Variable();
        if(tokens.valueIs("let")) {
            v.isConst = false;
        } else {
            v.isConst = true;
        }
        tokens.next();

        v.name = tokens.get().value;
        tokens.next();

        node.add(v);
    }
}
