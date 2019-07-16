package warp.ast.stmt;

import warp.ModuleState;
import warp.ast.ASTNode;

public abstract class Statement extends ASTNode {

    abstract public Statement parse(ModuleState state, ASTNode parent);

}
