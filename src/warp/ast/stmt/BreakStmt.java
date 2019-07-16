package warp.ast.stmt;

import warp.ModuleState;
import warp.ast.ASTNode;

final public class BreakStmt extends Statement {

    @Override
    public String toString() {
        return "break";
    }

    @Override
    public BreakStmt parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip("break");

        return this;
    }
}
