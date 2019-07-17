package warp.ast.decl.var;

import warp.ModuleState;
import warp.ast.ASTNode;

/**
 * MultiVariableDecl
 *      VariableDecl | VariableDestructuringDecl        (1 or more)
 */
final public class MultiVariableDecl extends AbsVariableDecl {

    @Override public String toString() {
        return isConst ? "const" : "let";
    }

    @Override public MultiVariableDecl parse(ModuleState state, ASTNode parent) {
        throw new RuntimeException("Should not be called");
    }
}
