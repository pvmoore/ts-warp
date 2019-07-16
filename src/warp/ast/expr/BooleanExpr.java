package warp.ast.expr;

import warp.ModuleState;
import warp.ast.ASTNode;

final public class BooleanExpr extends Expression {
    public boolean value;

    @Override public String toString() {
        return String.format("%s", value);
    }
    /**
     * true | false
     */
    @Override public BooleanExpr parse(ModuleState state, ASTNode parent) {
        this.value = state.tokens.value().equals("true");
        state.tokens.next();

        parent.add(this);
        return this;
    }
}
