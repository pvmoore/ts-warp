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

    public static void parse(ModuleState state, ASTNode parent) {
        log.trace("parse " + state.tokens.get());

        parseFirst(state, parent);
        parseSecond(state, parent);
    }

    private static Expression parseFirst(ModuleState state, ASTNode parent) {
        log.trace("parseFirst "+state.tokens.get());

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
            case PLUS2:
            case MINUS2:
                /* pre inc/dec */
                return new IncDecExpr().parse(state, parent);
            case PLUS:
            case MINUS:
            case TILDE:
            case EXCLAMATION:
                return new UnaryExpr().parse(state, parent);
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
            case "new":
                return new NewExpr().parse(state, parent);
        }

        /* Assume it's an identifier for now */
        if(t.kind == Token.Kind.IDENTIFIER) {
            return new IdentifierExpr().parse(state, parent);
        }

        throw new ParseError("Parse failed in file ["+state.file+"] @ "+tokens.get());
    }
    private static void parseSecond(ModuleState state, ASTNode parent) {
        log.trace("parseSecond "+state.tokens.get());
        var tokens = state.tokens;

        while(true) {
            switch(tokens.get().kind) {
                /* end of expression */
                case EOF:
                case SEMICOLON:
                case LBR:
                case RBR:
                case LSQBR:
                case RSQBR:
                case LCURLY:
                case RCURLY:
                case COMMA:
                case COLON:
                    return;

                case PLUS:
                case MINUS:
                case ASTERISK:
                case FWD_SLASH:
                case PERCENT:
                case AMPERSAND:
                case PIPE:
                case HAT:
                case LANGLE:
                case RANGLE:
                case LANGLE_EQ:
                case RANGLE_EQ:
                case AMPERSAND2:
                case PIPE2:

                case EQ2:
                case EQ3:
                case EXCLAMATION_EQ:
                case EXCLAMATION_EQ2:

                case EQ:
                case PLUS_EQ:
                case MINUS_EQ:
                case ASTERISK_EQ:
                case FWD_SLASH_EQ:
                case PERCENT_EQ:
                case AMPERSAND_EQ:
                case PIPE_EQ:
                case HAT_EQ:
                case LANGE2_EQ:
                case RANGLE2_EQ:
                case RANGLE3_EQ:
                    parent = attachAndRead(state, parent, new BinaryExpr().parse(state, parent));
                    break;
                case PLUS2:
                case MINUS2:
                    /* post inc/dec */
                    var expr = new IncDecExpr();
                    expr.isInc = tokens.get().kind == Token.Kind.PLUS2;
                    tokens.next();
                    parent = attach(state, parent, expr);
                    break;

                case IDENTIFIER:
                    if(tokens.isKeyword("instanceof")) {
                        parent = attachAndRead(state, parent, new InstanceofExpr().parse(state, parent));
                        break;
                    }
                    /* end of expression */
                    return;

                default:
                    throw new ParseError("parseSecond failed: " + tokens.get());
            }
        }
    }
    private static Expression attach(ModuleState state, ASTNode parent, Expression newExpr) {

        var prev = parent;
        if(prev instanceof Expression) {
            /// Adjust to account for operator precedence
            var prevExpr = (Expression)prev;

            while(prevExpr.hasParent() && newExpr.getPrecedence() <= prevExpr.getPrecedence()) {

                if(!(prevExpr.getParent() instanceof Expression)) {
                    prev = prevExpr.getParent();
                    break;
                }

                prevExpr = (Expression)prevExpr.getParent();
                prev     = prevExpr;
            }
        }

        newExpr.add(prev.lastChild());
        prev.add(newExpr);

        return newExpr;
    }
    private static Expression attachAndRead(ModuleState state, ASTNode parent, Expression newExpr) {
        log.trace("attachAndRead "+state.tokens.get()+" parent:"+parent+" new:"+newExpr);

        attach(state, parent, newExpr);

        parseFirst(state, newExpr);

        return newExpr;
    }
}
