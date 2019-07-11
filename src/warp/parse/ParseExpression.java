package warp.parse;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.Expression;
import warp.ast.expr.BooleanExpr;
import warp.ast.expr.NumberExpr;
import warp.ast.expr.StringExpr;

final public class ParseExpression {
    private static Logger log = Logger.getLogger(ParseExpression.class);

    public static Expression parse(ModuleState state, ASTNode parent) {
        log.trace("parse "+state.tokens.get());

        var tokens = state.tokens;
        var t = tokens.get();

        switch(t.kind) {
            case NUMBER:
                return new NumberExpr().parse(state, parent);
            case STRING:
                return new StringExpr().parse(state, parent);
        }

        switch(t.value) {
            case "true":
            case "false":
                return new BooleanExpr().parse(state, parent);
        }

        throw new ParseError("Parse failed in file ["+state.file+"] @ "+tokens.get());
    }
}
