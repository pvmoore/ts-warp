package warp.ast;

import warp.ModuleState;

public abstract class Statement extends ASTNode {

    abstract public Statement parse(ModuleState state, ASTNode parent);

}
