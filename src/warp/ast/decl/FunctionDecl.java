package warp.ast.decl;

import warp.Access;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.misc.Util;

final public class FunctionDecl extends Declaration {
    public String name;
    public Access access = Access.PUBLIC;   /* For class members only */


    boolean isClassMember() {
        return parent instanceof ClassDecl;
    }

    @Override public FunctionDecl parse(ModuleState state, ASTNode parent) {
        Util.todo();
        return this;
    }

    @Override public String toString() {
        return String.format("[FunctionDecl name:%s]", name);
    }
}
