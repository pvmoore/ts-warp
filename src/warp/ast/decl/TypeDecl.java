package warp.ast.decl;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.misc.Util;

final public class TypeDecl extends Declaration {
    public String name;

    @Override public String toString() {
        return String.format("[TypeDecl name:%s]", name);
    }

    @Override public TypeDecl parse(ModuleState state, ASTNode parent) {
        Util.todo();
        return this;
    }
}
