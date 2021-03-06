package warp.ast.expr;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.parse.ParseExpression;

/**
 * TypeExpr
 *      IdentifierExpr
 */
final public class TypeExpr extends Expression {

    @Override public int getPrecedence() {
        return 1;
    }
    @Override
    public String toString() {
        return "type";
    }

    /**
     * identifier
     */
    @Override
    public TypeExpr parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        ParseExpression.parse(state, this);

        return this;
    }
}
