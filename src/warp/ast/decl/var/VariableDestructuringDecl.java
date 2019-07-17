package warp.ast.decl.var;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.decl.DestructuringDecl;

final public class VariableDestructuringDecl extends AbsVariableDecl {
    private DestructuringDecl destructuringHandler = new DestructuringDecl();

    @Override
    public String toString() {
        var c  = isConst ? "const " : "let ";
        return c + destructuringHandler.toString();
    }

    @Override
    public VariableDestructuringDecl parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        /* Optional if part of a MultiVariableDecl */
        if(tokens.isValue("const")) {
            this.isConst = true;
            tokens.next();
        } else if(tokens.isValue("let")) {
            tokens.next();
        }

        destructuringHandler.parse(state, this);

        return this;
    }
}
