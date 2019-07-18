package warp.ast.expr;

import warp.ModuleState;
import warp.ast.ASTNode;

/**
 *
 */
final public class InstanceofExpr extends Expression {

    @Override
    public int getPrecedence() {
        return 11;
    }

    @Override public String toString() {
        return "instanceof";
    }
    /**
     * 'instanceof' Expression
     */
    @Override
    public InstanceofExpr parse(ModuleState state, ASTNode parent) {
        /* Don't add to parent. This is handled in ParseExpression.attach */
        var tokens = state.tokens;

        tokens.skip("instanceof");

        return this;
    }
}
