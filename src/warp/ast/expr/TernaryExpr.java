package warp.ast.expr;

import warp.ModuleState;
import warp.Operator;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseExpression;

/**
 *  TernaryExpr
 *      Expression  (then)
 *      Expression  (else)
 *      Expression  (condition)         <-- note condition is last
 */
final public class TernaryExpr extends Expression {

    @Override
    public int getPrecedence() {
        return Operator.TERNARY.precedence;
    }

    @Override public String toString() {
        return "?:";
    }

    /**
     *  Expression '?' Expression ':' Expression
     */
    @Override
    public TernaryExpr parse(ModuleState state, ASTNode parent) {
        log.trace("parse "+state.tokens.get());
        /* Don't add to parent. This is handled in ParseExpression.attach */
        var tokens = state.tokens;

        tokens.skip(Token.Kind.QUESTION);

        ParseExpression.parseIsolated(state, this);

        tokens.skip(Token.Kind.COLON);

        ParseExpression.parseIsolated(state, this);

        return this;
    }
}
