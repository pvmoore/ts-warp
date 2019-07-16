package warp.ast.stmt;

import warp.ModuleState;
import warp.ast.ASTNode;

final public class ContinueStmt extends Statement {

    @Override
    public String toString() {
        return "continue";
    }

    @Override
    public ContinueStmt parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip("continue");

        return this;
    }
}
