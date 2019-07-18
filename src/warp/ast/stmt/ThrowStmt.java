package warp.ast.stmt;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.parse.ParseExpression;

/**
 * ThrowStmt
 *      Expression
 */
final public class ThrowStmt extends Statement {

    @Override public String toString() {
        return "throw";
    }

    /**
     * 'throw' Expression
     */
    @Override
    public Statement parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip("throw");

        ParseExpression.parse(state, this);

        return this;
    }
}
