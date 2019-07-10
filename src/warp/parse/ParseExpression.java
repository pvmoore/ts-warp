package warp.parse;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.expr.BooleanExpr;
import warp.ast.expr.NumberExpr;
import warp.ast.expr.StringExpr;

final public class ParseExpression {
    private static Logger log = Logger.getLogger(ParseExpression.class);

    public static void parse(ModuleState state, ASTNode parent) {
        log.trace("parse "+state.tokens.get());

        var tokens = state.tokens;
        var t = tokens.get();

        switch(t.kind) {
            case NUMBER:
                new NumberExpr().parse(state, parent);
                return;
            case STRING:
                new StringExpr().parse(state, parent);
                return;
        }

        switch(t.value) {
            case "true":
            case "false":
                new BooleanExpr().parse(state, parent);
                return;
        }


        //0x
        //0o

        throw new ParseError("Parse failed in file ["+state.file+"] @ "+tokens.get());
    }
}
