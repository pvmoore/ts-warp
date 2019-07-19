package warp.ast.stmt;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.decl.Declaration;

/**
 * NamespaceDecl
 *      BlockStmt
 */
final public class NamespaceDecl extends Declaration {
    public String name;

    @Override
    public String toString() {
        return "namespace "+name;
    }

    /**
     * 'namespace' BlockStmt
     */
    @Override
    public NamespaceDecl parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip("namespace");

        this.name = tokens.value(); tokens.next();

        new BlockStmt().parse(state, this);

        return this;
    }
}
