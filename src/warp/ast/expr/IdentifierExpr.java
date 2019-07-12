package warp.ast.expr;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.Expression;

final public class IdentifierExpr extends Expression {
    public String name;

    @Override public IdentifierExpr parse(ModuleState state, ASTNode parent) {
        var tokens = state.tokens;

        this.name = tokens.value();

        tokens.next();

        return this;
    }

    @Override public String toString() {
        return String.format("[identifier %s]", name);
    }
}
