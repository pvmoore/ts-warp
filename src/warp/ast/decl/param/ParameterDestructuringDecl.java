package warp.ast.decl.param;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.decl.DestructuringDecl;

final public class ParameterDestructuringDecl extends AbsParameterDecl {
    private DestructuringDecl destructuringHandler = new DestructuringDecl();

    @Override
    public String toString() {
        return destructuringHandler.toString();
    }

    @Override
    public ParameterDestructuringDecl parse(ModuleState state, ASTNode parent) {
        parent.add(this);

        destructuringHandler.parse(state, this);

        return this;
    }
}
