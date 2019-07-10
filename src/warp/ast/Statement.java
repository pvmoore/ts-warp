package warp.ast;

import warp.ModuleState;

public abstract class Statement extends ASTNode {

    abstract public void parse(ModuleState state, ASTNode parent);

}
