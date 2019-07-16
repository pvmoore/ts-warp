package warp.parse;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.expr.Expression;
import warp.ast.expr.*;
import warp.lex.Token;

/**
 * https://github.com/Microsoft/TypeScript/blob/master/doc/spec.md#a2-expressions
 */
final public class ParseExpression {
    final private static Logger log = Logger.getLogger(ParseExpression.class);

    public static Expression parse(ModuleState state, ASTNode parent) {
        log.trace("parse "+state.tokens.get());

        var tokens = state.tokens;
        var t = tokens.get();

        switch(t.kind) {
            case NUMBER:
                return new NumberExpr().parse(state, parent);
            case STRING:
                return new StringExpr().parse(state, parent);
            case LCURLY:
                return new ObjectExpr().parse(state, parent);
            case LBR:
                /* Could be ParensExpr or FunctionExpr */
                var end = tokens.findClosingBr();
                if(end!=-1) {
                    var after = tokens.peek(end+1).kind;

                    // (...) =>
                    // (...) : Type

                    if(after== Token.Kind.RARROW || after== Token.Kind.COLON) {
                        return new FunctionExpr().parse(state, parent);
                    }
                }
                return new ParensExpr().parse(state, parent);
        }

        switch(t.value) {
            case "true":
            case "false":
                return new BooleanExpr().parse(state, parent);
            case "null":
                return new NullExpr().parse(state, parent);
            case "function":
                return new FunctionExpr().parse(state, parent);
            case "typeof":
                return new TypeofExpr().parse(state, parent);
        }

        /* Assume it's an identifier for now */
        if(t.kind == Token.Kind.IDENTIFIER) {
            return new IdentifierExpr().parse(state, parent);
        }

        throw new ParseError("Parse failed in file ["+state.file+"] @ "+tokens.get());
    }
}
