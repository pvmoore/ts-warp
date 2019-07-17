package warp.ast.expr;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseExpression;

/**
 * IncDecExpr
 *      Expression
 */
final public class IncDecExpr extends Expression {
    public boolean isInc;
    public boolean isPre;

    @Override public int getPrecedence() {
        return isPre ? 16 : 17;
    }
    @Override
    public String toString() {
        var s = isInc ? "++" : "--";
        var pd = isPre ? "pre " : "post ";
        return pd+s;
    }

    @Override
    public IncDecExpr parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        /* Consume the ++ or -- */

        if(tokens.kind()== Token.Kind.PLUS2) {
            isInc = true;
            isPre = true;
            tokens.next();
        } else if(tokens.kind()== Token.Kind.MINUS2) {
            isPre = true;
            tokens.next();
        }

        ParseExpression.parse(state, this);

        return this;
    }
}
