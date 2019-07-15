package warp.ast.decl.var;

import warp.ast.decl.Declaration;

abstract public class AbsVariableDecl extends Declaration {

    public boolean isConst;

    public boolean isInitialised() {
        return firstChild() != null;
    }
}
