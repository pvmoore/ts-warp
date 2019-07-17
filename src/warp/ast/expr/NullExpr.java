package warp.ast.expr;

import warp.ModuleState;
import warp.ast.ASTNode;

final public class NullExpr extends Expression {

    @Override public int getPrecedence() {
        return 1;
    }
    @Override public String toString() {
        return "[null]";
    }

    /**
     * null
     */
    @Override public NullExpr parse(ModuleState state, ASTNode parent) {

        state.tokens.next();

        parent.add(this);

        return this;
    }
}
