package warp.ast.stmt;

import warp.ModuleState;
import warp.ast.ASTNode;

final public class NoopStmt extends Statement {

    @Override public String toString() {
        return "no-op";
    }

    @Override
    public NoopStmt parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        return this;
    }
}
