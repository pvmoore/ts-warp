package warp.ast.decl;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.Statement;
import warp.misc.Util;

final public class ClassDecl extends Statement {
    public String name;

    @Override public ClassDecl parse(ModuleState state, ASTNode parent) {
        Util.todo();
        return this;
    }

    @Override public String toString() {
        return String.format("[ClassDecl name:%s]", name);
    }
}
