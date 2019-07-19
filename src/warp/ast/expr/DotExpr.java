package warp.ast.expr;

import warp.ModuleState;
import warp.Operator;
import warp.ast.ASTNode;
import warp.lex.Token;

/**
 * DotExpr
 *      Expression
 *      Expression
 */
final public class DotExpr extends Expression {

    @Override
    public int getPrecedence() {
        return Operator.DOT.precedence;
    }

    @Override
    public String toString() {
        return ".";
    }

    @Override
    public DotExpr parse(ModuleState state, ASTNode parent) {
        /* Don't add to parent. This is handled in ParseExpression.attach */
        var tokens = state.tokens;
        tokens.skip(Token.Kind.DOT);
        return this;
    }
}
