package warp.ast.expr;

import warp.ModuleState;
import warp.ast.ASTNode;

final public class IdentifierExpr extends Expression {
    public String name;

    @Override public String toString() {
        return String.format("%s", name);
    }

    @Override public IdentifierExpr parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        this.name = tokens.value();
        tokens.next();

        return this;
    }
}
