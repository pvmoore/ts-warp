package warp.ast.decl;

import warp.Access;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.Statement;
import warp.misc.Util;

final public class FunctionDecl extends Statement {
    public String name;
    public Access access = Access.PUBLIC;   /* For class members only */


    boolean isClassMember() {
        return parent instanceof ClassDecl;
    }

    @Override public void parse(ModuleState state, ASTNode parent) {
        Util.todo();
    }

    @Override public String toString() {
        return String.format("[FunctionDecl name:%s]", name);
    }
}
